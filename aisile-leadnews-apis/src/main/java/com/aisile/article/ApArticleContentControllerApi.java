package com.aisile.article;

import com.aisile.model.article.pojos.ArticleContent;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
public interface ApArticleContentControllerApi {
    /**
     * 保存apArticleContent  （feign）
     * @param apArticleContent
     * @return
     */
    public ArticleContent saveApArticleContent(ArticleContent apArticleContent);
}
