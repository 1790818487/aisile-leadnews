package com.aisile.service.impl;

import com.aisile.mapper.ApAuthorMapper;
import com.aisile.mapper.ArticleConfigMapper;
import com.aisile.mapper.ArticleContentMapper;
import com.aisile.model.article.pojos.Article;
import com.aisile.mapper.ArticleMapper;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.service.IArticleService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private ApAuthorMapper apAuthorMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private ArticleConfigMapper articleConfigMapper;

    @Override
    public ResponseResult saveArticle(List<WmNews> list) {

        for (WmNews wmNews : list) {
            /*
             *
             * 保存到已发布文章
             */
            Article article = new Article()
                    .setTitle(wmNews.getTitle())
                    .setChannelId(wmNews.getChannelId())
                    .setPublishTime(wmNews.getPublishTime())
                    .setCreatedTime(new Date())
                    .setAuthorId(apAuthorMapper.selectById(wmNews.getUserId()).getId());
            articleMapper.insert(article);

            /*
             *
             *将已经发布的文章的内容保存
             *
             */

            ArticleContent content = new ArticleContent()
                    .setContent(wmNews.getContent())
                    .setArticleId(article.getId());
            articleContentMapper.insert(content);

            /*
            把发布的文章的配置保存下来
             */
            ArticleConfig config = new ArticleConfig()
                    .setArticleId(article.getId())
                    .setIsComment(true)
                    .setIsComment(true)
                    .setIsDelete(false)
                    .setIsDown(false)
                    .setIsForward(true);
            articleConfigMapper.insert(config);
        }

        return null;
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @Override
    public ResponseResult delById(int id) {
        //删除三个表的相关数据关系
        articleConfigMapper.delete(
                Wrappers.lambdaQuery(new ArticleConfig())
                        .eq(ArticleConfig::getArticleId, id)
        );
        articleContentMapper.delete(
                Wrappers.lambdaQuery(new ArticleContent())
                        .eq(ArticleContent::getArticleId, id)
        );
        this.removeById(id);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
