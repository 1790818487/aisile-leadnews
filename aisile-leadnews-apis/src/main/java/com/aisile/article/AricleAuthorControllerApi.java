package com.aisile.article;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.article.pojos.Article;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.model.common.dtos.ResponseResult;

public interface AricleAuthorControllerApi {
    ApAuthor saveArticle(ApAuthor apAuthor);

    //通过user_id找到文章作者
    ApAuthor findByUserId(int user_id);

}
