package com.aisile.controller.v1;


import com.aisile.article.ApArticleContentControllerApi;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.service.IArticleContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * APP已发布文章内容表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Controller
@RequestMapping("/api/article/v1/content")
public class ArticleContentController implements ApArticleContentControllerApi {
    @Autowired
    private IArticleContentService apArticleContentService;

    @Override
    @PostMapping("/save")
    public ArticleContent saveApArticleContent(@RequestBody ArticleContent apArticleContent) {
        apArticleContentService.save(apArticleContent);
        return apArticleContent;
    }
}
