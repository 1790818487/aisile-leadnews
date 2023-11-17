package com.aisile.lisener;

import com.aisile.mapper.WmNewsMapper;
import com.aisile.mapper.WmNewsMaterialMapper;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.model.media.pojos.WmNewsMaterial;
import com.aisile.utils.common.AppJwtUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AutoArticleExamine {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @RabbitListener(queues = "queen.article")
    public void ExamineArticle(Integer id) {
        //拿到wm-news 的id，查询到所有的数据，
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null)
            return;
        //再查出来这个图文引用的图片，进行图片审核，
        List<WmNewsMaterial> wmNewsMaterials = wmNewsMaterialMapper.selectList(
                Wrappers.lambdaQuery(new WmNewsMaterial())
                        .eq(WmNewsMaterial::getNewsId, wmNews.getId())
                        .select(WmNewsMaterial::getMaterialId)
        );
        //拿到了wm_news_material中的material_id直接去wm_material查询
//        并对查到的图片审核，违规直接修改wm_news中status 的状态为2
//        有疑似的时候给人工审核去

        /**
         * 直接设置为疑似,让管理员去审核
         */
        wmNews.setStatus((short) 3);
        wmNews.setReason("存在疑似违规元素,需要等待人工审核");
        wmNewsMapper.updateById(wmNews);

//        使用RestTemplate的方式
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type","application/json");
//        headers.set("token", AppJwtUtil.getToken(System.currentTimeMillis()));
//        restTemplate.postForObject("", wmNews.getId(),)


    }
}
