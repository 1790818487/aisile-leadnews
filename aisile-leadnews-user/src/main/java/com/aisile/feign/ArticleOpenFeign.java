package com.aisile.feign;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.HeaderParam;

@FeignClient("article-service")
public interface ArticleOpenFeign {
    //     /api/apauthor
    @PostMapping(value = "/api/apauthor/add")
    ResponseResult saveArticle(ApAuthor apAuthor);


    @GetMapping(value = "find/{id}")
    ApAuthor findByUserId(@PathVariable("id") int user_id);

}
