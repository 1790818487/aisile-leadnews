package com.aisile.feign;

import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("article-service")
public interface ArticleFeign {

    @DeleteMapping("/api/article/v1/article/delByArticle/{id}")
    ResponseResult delArticleByIdForFeign(@PathVariable int id);

}
