package com.aisile.article;

import com.aisile.model.article.pojos.ArticleConfig;


/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
public interface ApArticleConfigControllerApi {

    /**
     * 保存articleConfig  （feign）
     * @param apArticleConfig
     * @return
     */
     ArticleConfig saveArticleConfig(ArticleConfig apArticleConfig);
}
