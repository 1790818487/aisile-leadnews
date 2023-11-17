package com.aisile.controller.v1;


import com.aisile.article.ApArticleConfigControllerApi;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.service.IArticleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * APP已发布文章配置表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Controller
@RequestMapping("/api/article/v1/config")
public class ArticleConfigController implements ApArticleConfigControllerApi {

    @Autowired
    private IArticleConfigService articleConfigService;

    @Override
    @PostMapping("/save")
    public ArticleConfig saveArticleConfig(@RequestBody ArticleConfig apArticleConfig) {
        articleConfigService.save(apArticleConfig);
        return apArticleConfig;
    }
}
