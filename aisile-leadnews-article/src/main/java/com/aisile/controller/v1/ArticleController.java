package com.aisile.controller.v1;


import com.aisile.article.ApArticleControllerApi;
import com.aisile.model.article.pojos.Article;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import org.apache.ibatis.ognl.ASTThisVarRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Controller
@RequestMapping("/api/article/v1/article")
public class ArticleController implements ApArticleControllerApi {

    @Autowired
    private IArticleService apArticleService;

    @Override
    @PostMapping("/save")
    public boolean saveArticle(@RequestBody WmNews wmNews) {
        Article article = new Article();
        article.setTitle(wmNews.getTitle());
        return apArticleService.save(article);
    }


    @Override
    @PostMapping("/save-publish")
    public ResponseResult saveArticlePublish(@RequestBody List<WmNews> wmNews) {
        return apArticleService.saveArticle(wmNews);
    }

    @Override
    @DeleteMapping("delByArticle/{id}")
    public ResponseResult delArticleByIdForFeign(@PathVariable int id) {
        return apArticleService.delById(id);
    }
}
