package com.aisile.article;

import com.aisile.model.article.pojos.Article;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmNews;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
public interface ApArticleControllerApi {

    public boolean saveArticle(WmNews wmNews);

    public ResponseResult saveArticlePublish(@RequestBody List<WmNews> wmNews);

    ResponseResult delArticleByIdForFeign(int id);
}
