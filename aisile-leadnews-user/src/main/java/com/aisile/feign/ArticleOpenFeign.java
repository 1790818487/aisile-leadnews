package com.aisile.feign;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient("article-service")
public interface ArticleOpenFeign {
    //     /api/apauthor
    @PostMapping(value = "/api/apauthor/add")
    ApAuthor saveArticle(ApAuthor apAuthor);


    @GetMapping(value = "/api/apauthor/find/{id}")
    ApAuthor findByUserId(@PathVariable("id") int user_id);

}
