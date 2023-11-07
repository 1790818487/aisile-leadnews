package com.aisile.article;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.common.dtos.ResponseResult;

public interface AricleAuthorControllerApi {
    ApAuthor saveArticle(ApAuthor apAuthor);

    ApAuthor findByUserId(int user_id);
}
