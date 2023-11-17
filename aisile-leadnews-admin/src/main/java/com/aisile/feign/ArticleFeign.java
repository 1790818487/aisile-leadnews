package com.aisile.feign;

import com.aisile.config.OpenFeignConfig;
import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.article.pojos.Article;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmNews;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
@FeignClient(value = "article-service")
public interface ArticleFeign {
//
    @GetMapping("/api/article/v1/apauthor/find/{id}")
    ApAuthor findById(@PathVariable("id") Integer id);

    @PostMapping("/api/article/v1/article/save")
    Article saveArticle(@RequestBody Article apArticle);

    @PostMapping("/api/article/v1/content/save")
    ArticleContent saveApArticleContent(@RequestBody ArticleContent apArticleContent);

    @PostMapping("/api/article/v1/config/save")
    ArticleConfig saveArticleConfig(@RequestBody ArticleConfig apArticleConfig);

    @PostMapping("/api/article/v1/article/save-publish")
    ResponseResult saveArticlePublish(@RequestBody List<WmNews> wmNews);
}
