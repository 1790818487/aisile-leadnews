package com.aisile.service.impl;

import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.feign.ArticleFeign;
import com.aisile.feign.WemediaFeign;
import com.aisile.mapper.AdChannelMapper;
import com.aisile.mapper.SensitiveMapper;
import com.aisile.model.admin.pojos.AdChannel;
import com.aisile.model.admin.pojos.AdSensitive;
import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.article.pojos.Article;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.model.media.pojos.WmUser;
import com.aisile.service.WemediaNewsAutoScanService;
import com.aisile.utils.common.SensitiveWordUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
@Service
public class WemediaNewsAutoScanServiceImpl implements WemediaNewsAutoScanService {

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private SensitiveMapper adSensitiveMapper;

//    @Autowired
//    private GreenTextForContentScan greenTextForContentScan;
//
//    @Autowired
//    private GreenImagesForUrlScan greenImagesForUrlScan;

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Autowired
    private ArticleFeign articleFeign;


    /**
     * 自媒体文章审核方法  （人工审核、自动审核、定时发布）
     *
     * @param id 自媒体文章id  rabbitMQ 消息传递
     */
    @Override
    public void autoScanByMediaNewsId(Integer id) {
        if (id == null) {
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 实现远程调用
        WmNews wmNews = wemediaFeign.findByIdToFeign(id);
        if (wmNews == null) {
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 开始实现业务逻辑   文章状态 = 4  证明文章已经审核通过  直接发布即可（保存三张表信息）
        if (wmNews.getStatus().equals((short) 4)) {
            saveAppArticleInfo(wmNews);
            return;
        }
        if (wmNews.getPublishTime() != null) {
            if (wmNews.getStatus().equals((short) 8) && (wmNews.getPublishTime().getTime() <= System.currentTimeMillis())) {
                saveAppArticleInfo(wmNews);
                return;
            }
        }

        // 状态  = 1   > 调用敏感词管理  文本发垃圾检查  图片审核
        if (wmNews.getStatus().equals((short) 1)) {
            Map<String, List<String>> textsAndImages = this.getTextAndImages(wmNews);
            /**
             * [{'type':'text',
             * 'value':''},
             * {'type':'image',
             * 'value':''}]
             */

            //List<String> texts  需要检测的文本内容
            List<String> texts = textsAndImages.get("texts");
            List<String> images = textsAndImages.get("images");
            if (!checkSensitive(texts, wmNews)) {
                return;
            }
            // 调用阿里云文本反垃圾检测
            if (!checkTextScanAliyun(texts, wmNews)) {
                return;
            }
            // 调用阿里云图片检测
            if (!checkImaegsScanAliyun(images, wmNews)) {
                return;
            }
            // 没有任何毛病  然后看下发布时间
            if (wmNews.getPublishTime() != null) {
                if (wmNews.getPublishTime().getTime() > System.currentTimeMillis()) {
                    this.updateWmNews(wmNews, (short) 8, "审核成功，没有到指定发布时间，待发布");
                    return;
                }
            }
            this.saveAppArticleInfo(wmNews);
        }

    }

    /**
     * 根据文章内容、标题、封面图片 提取  待审核  文字以及图片
     *
     * @param wmNews
     * @return
     */
    private Map<String, List<String>> getTextAndImages(WmNews wmNews) {
        String str = wmNews.getContent();
        List<Map> contents = JSON.parseArray(str, Map.class);
        List<String> texts = new ArrayList<>();
        List<String> images = new ArrayList<>();
        // 内容
        contents.stream().forEach(x -> {
            if ("image".equals(x.get("type"))) {
                images.add((String) x.get("value"));
            }
            if ("text".equals(x.get("type"))) {
                texts.add((String) x.get("value"));
            }
        });
        // 标题
        if (wmNews.getTitle() != null) {
            texts.add(wmNews.getTitle());
        }
        // 封面图片
        if (wmNews.getImages() != null && wmNews.getType() != 0) {
            String[] strings = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(strings));
        }
        Map<String, List<String>> resultMap = new HashMap<>();
        resultMap.put("texts", texts);
        resultMap.put("images", images);
        return resultMap;
    }


    /**
     * 阿里云图片检测
     *
     * @param Imaegs
     * @param wmNews
     * @return
     */
    private boolean checkImaegsScanAliyun(List<String> Imaegs, WmNews wmNews) {
        boolean flag = true;
        //
        Integer textScore = 1;//greenImagesForUrlScan.imagesScan(Imaegs);
        if (textScore >= 0) {
            if (textScore != 0) {
                // 疑似
                this.updateWmNews(wmNews, (short) 3, "文章审核出现疑似问题，需要人工审核");
            }
        } else {
            this.updateWmNews(wmNews, (short) 2, "文章中包含违规媒体素材");
            flag = false;
        }
        return flag;
    }

    /**
     * 阿里云文本反垃圾检测
     *
     * @param texts
     * @param wmNews
     * @return
     */
    private boolean checkTextScanAliyun(List<String> texts, WmNews wmNews) {
        boolean flag = true;
        //
        Integer textScore = 1;//greenTextForContentScan.textScan(texts);
        if (textScore >= 0) {
            if (textScore != 0) {
                // 疑似
                this.updateWmNews(wmNews, (short) 3, "文章审核出现疑似问题，需要人工审核");
            }
        } else {
            this.updateWmNews(wmNews, (short) 2, "文章中包含平台禁用敏感词");
            flag = false;
        }
        return flag;
    }

    /**
     * 敏感词 检测
     *
     * @param texts
     * @param wmNews
     * @return
     */
    private boolean checkSensitive(List<String> texts, WmNews wmNews) {
        boolean flag = true;
        // 查询敏感词  在通过initmap 构建DFA算法的和
        LambdaQueryWrapper<AdSensitive> wrapper = Wrappers.lambdaQuery(new AdSensitive())
                .select(AdSensitive::getSensitives);
        List<String> list = adSensitiveMapper.selectObjs(wrapper).stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        // 敏感词
        SensitiveWordUtil.initMap(list);
        StringBuilder sb = new StringBuilder();
        for (String text : texts) {
            sb.append(text);
        }
        Map<String, Integer> map = SensitiveWordUtil.matchWords(sb.toString());
        if (map.size() > 0) {
            this.updateWmNews(wmNews, (short) 2, "文章中包含平台禁用敏感词");
            flag = false;
        }
        return flag;

    }

    /**
     * 保存文章三张表信息以及 修改文章审核状态（发布成功）
     *
     * @param wmNews
     */
    private void saveAppArticleInfo(WmNews wmNews) {
        // 保存文章主体
        Article apArticle = saveAparticle(wmNews);
        // 保存文章配置信息
        this.saveApArticleConfig(apArticle);
        // 保存文章内容
        this.saveApArticleContent(apArticle, wmNews);
        // 反向维护 wmnews表中的  文章库id
        wmNews.setArticleId(apArticle.getId());
        // 调用审核修改状态方法‘
        this.updateWmNews(wmNews, (short) 9, "审核成功！");
        // 同步索引库（待完成）
    }


    /**
     * 公用的审核状态方法  status = 2  3   8   9
     *
     * @param wmNews
     * @param status
     * @param msg
     */
    private void updateWmNews(WmNews wmNews, Short status, String msg) {
        if (msg != null) {
            wmNews.setReason(msg);
        }
        wmNews.setStatus(status);
        wemediaFeign.updateWmNewsFroFeign(wmNews);
    }


    /**
     * 保存文章主体信息
     *
     * @param wmNews
     * @return
     */
    private Article saveAparticle(WmNews wmNews) {
        Article apArticle = new Article();
        apArticle.setTitle(wmNews.getTitle());
        apArticle.setChannelId(wmNews.getChannelId());
        apArticle.setLayout(wmNews.getType());
        apArticle.setFlag(0);
        apArticle.setImages(wmNews.getImages());
        apArticle.setLabels(wmNews.getLabels());
        apArticle.setLikes(0);
        apArticle.setCollection(0);
        apArticle.setComment(0);
        apArticle.setViews(0);
        apArticle.setCreatedTime(wmNews.getCreatedTime());
        apArticle.setPublishTime(new Date());
        // 处理 作者  频道
        AdChannel adChannel = adChannelMapper.selectById(apArticle.getChannelId());
        if (adChannel == null) {
            apArticle.setChannelName("其他");
        } else {
            apArticle.setChannelName(adChannel.getName());
        }

        // 根据用户id查询 自媒体人信息
        WmUser wmUser = wemediaFeign.findByUserId(wmNews.getUserId());
        if (wmUser != null) {
            ApAuthor apAuthor = articleFeign.findById(wmUser.getApAuthorId());
            if (apAuthor != null) {
                apArticle.setAuthorId((int) apAuthor.getId().longValue());
                apArticle.setAuthorName(apAuthor.getName());
            } else {
                apArticle.setAuthorId(0);
                apArticle.setAuthorName("匿名");
            }
        } else {
            apArticle.setAuthorId(0);
            apArticle.setAuthorName("匿名");
        }
        return articleFeign.saveArticle(apArticle);
    }

    /**
     * 保存文章配置信息
     *
     * @param apArticle
     */
    private void saveApArticleConfig(Article apArticle) {
        ArticleConfig apArticleConfig = new ArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
        apArticleConfig.setIsComment(true);
        apArticleConfig.setIsForward(true);
        apArticleConfig.setIsDown(false);
        apArticleConfig.setIsDelete(false);
        articleFeign.saveArticleConfig(apArticleConfig);
    }

    /**
     * 保存文章文本信息
     *
     * @param apArticle
     * @param wmNews
     */
    private void saveApArticleContent(Article apArticle, WmNews wmNews) {
        ArticleContent apArticleContent = new ArticleContent();
        apArticleContent.setContent(wmNews.getContent());
        apArticleContent.setArticleId(apArticle.getId());
        articleFeign.saveApArticleContent(apArticleContent);
    }

}
