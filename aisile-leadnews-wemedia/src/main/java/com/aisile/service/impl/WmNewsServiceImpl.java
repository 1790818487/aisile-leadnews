package com.aisile.service.impl;

import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.feign.ArticleFeign;
import com.aisile.mapper.WmMaterialMapper;
import com.aisile.mapper.WmNewsMaterialMapper;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.dtos.WmNewsDto;
import com.aisile.model.media.dtos.WmNewsPageReqDto;
import com.aisile.model.media.pojos.WmMaterial;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.mapper.WmNewsMapper;
import com.aisile.model.media.pojos.WmNewsMaterial;
import com.aisile.rabbitmqUtil.RabbitMqUtils;
import com.aisile.service.IWmNewsService;
import com.aisile.utils.threadlocal.WmThreadLocalUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * <p>
 * 自媒体图文内容信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements IWmNewsService {

    @Override
    public ResponseResult showAllContext(WmNewsPageReqDto dto) {
        if (dto == null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_REQUIRE);
        dto.checkParam();
        dto.checkParams();

        //判断是不是传用户id进来了
        if (WmThreadLocalUtils.getUser() == null || WmThreadLocalUtils.getUser().getApUserId() == null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.UNKNOWN_USER);

        //注入查询的条件
        LambdaQueryWrapper<WmNews> wrapper = Wrappers.lambdaQuery(new WmNews())
                .select(WmNews::getId,WmNews::getTitle,WmNews::getStatus,
                        WmNews::getCreatedTime,WmNews::getImages,WmNews::getUserId,
                        WmNews::getChannelId,WmNews::getArticleId)
                .eq(WmNews::getUserId, WmThreadLocalUtils.getUser().getApUserId())
                .in(dto.getStatus()==1,WmNews::getStatus, Arrays.asList(1,3))
                .in(dto.getStatus()==9,WmNews::getStatus, Arrays.asList(9,8,4))
                .eq(dto.getStatus()==0,WmNews::getStatus, 0)
                .eq(dto.getStatus()==2,WmNews::getStatus, 2)
                .like(dto.getKeyword() != null, WmNews::getTitle, dto.getKeyword())
                .eq(dto.getChannelId() != null, WmNews::getChannelId, dto.getChannelId())
                .between(dto.getBeginPubDate() != null && dto.getEndPubDate() != null,
                        WmNews::getCreatedTime, dto.getBeginPubDate(), dto.getEndPubDate())
                .orderByDesc(WmNews::getCreatedTime);

        IPage<WmNews> page = this.page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        //把得到结果封装到自己的方法中
        PageResponseResult result = new PageResponseResult((int) page.getCurrent(),
                (int) page.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    @Autowired
    private ArticleFeign articleFeign;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult addNews(WmNewsDto dto) {

        if (dto == null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_REQUIRE);

        //这里做用户添加自己需要发布的文章
        if (dto.getStatus() != 0 && dto.getStatus() != 1)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_INVALID);
        WmNews wmNews = new WmNews();
        wmNews.setCreatedTime(new Date());
        wmNews.setUserId(WmThreadLocalUtils.getUser().getApUserId());
        wmNews.setTitle(dto.getTitle());
        wmNews.setContent(JSON.toJSONString(dto.getContent()));
        wmNews.setType(dto.getType());
        wmNews.setChannelId(dto.getChannelId());
        wmNews.setLabels(dto.getLabels());
        wmNews.setSubmitedTime(new Date());
        wmNews.setStatus(dto.getStatus());
        //图片之间用逗号分隔
        wmNews.setImages(String.join(",",dto.getImages()));
        if (dto.getPublishTime() != null)
            wmNews.setPublishTime(dto.getPublishTime());
        else
            wmNews.setPublishTime(new Date());
        //直接保存
        this.save(wmNews);

        //判断是不是有图片,有图才进行保存
        if (dto.getImages().size() > 0) {
            //通过user_id去wm_material查询和dto.getImages()集合相同的素材id.
            List<WmMaterial> wmMaterialsId = wmMaterialMapper.selectList(
                    Wrappers.lambdaQuery(new WmMaterial())
                            .eq(WmMaterial::getUserId, WmThreadLocalUtils.getUser().getApUserId())
                            .in(WmMaterial::getUrl, dto.getImages())
                            .select(WmMaterial::getId)
            );
            //创建WmNewsMaterial对象,里面只需要两个值,素材id和wm_news的id,然后进行保存
            wmMaterialsId.forEach(a -> {
                WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
                wmNewsMaterial.setNewsId(wmNews.getId());
                wmNewsMaterial.setMaterialId(a.getId());
                wmNewsMaterial.setType((short) 0);
                wmNewsMaterialMapper.insert(wmNewsMaterial);
            });
        }

        //把保存好的数据的id保存到mq消息队列中去执行自动审核，只有当状态等于1才放进去
//        //判断一下是0或者1,草稿0不用审核,只需要保存就行
        if (dto.getStatus() == 1) {
            //需要调用接口对图文进行审核,违规直接就把状态设置未2
            //如果疑似则设置状态3,交给人工审核
            System.out.println("进行自动审核,判断图文是不是违规了");
            wmNews.setStatus((short) 8);
            this.updateById(wmNews);

            //审核完成后，对比时间，如果有发布时间，直接就放到mq队列中，如果时间比当前时间小，也放到队列中，把时间设置为0，立即执行
            if (dto.getPublishTime() != null) {
                long time = dto.getPublishTime().getTime() - System.currentTimeMillis();
                if (time < 0)
                    time = 0;
                RabbitMqUtils.sendDelayMessage(rabbitTemplate,
                        Integer.parseInt(String.valueOf(time)),
                        wmNews.getId(), (long)wmNews.getId());
            }

            //假设审核通过,没有违规
//            rabbitTemplate.convertAndSend("queen.article",wmNews.getId());


        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * @param id wmnews的id.用来进行文章的删除
     * @return: com.aisile.model.common.dtos.ResponseResult
     */
    @Override
    public ResponseResult delWmNewsById(int id) {
        //先查询出来这个文章的信息,判断是不是已经发布了
        WmNews wmNews = this.getById(id);
        //判断发布的状态,如果是9,需要远程调用数据库leadnews_article,删除已经发布的文章
        if (wmNews.getStatus() == 9&&wmNews.getArticleId()!=null)
            articleFeign.delArticleByIdForFeign(Integer.parseInt(wmNews.getArticleId().toString()));
        else
            this.removeById(id);
        //需要删除wm_news_material表中的关系,通过news_id判断,传值为wm_news中的id
        wmNewsMaterialMapper.delete(Wrappers.lambdaQuery(new WmNewsMaterial())
                .eq(WmNewsMaterial::getNewsId, id));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
