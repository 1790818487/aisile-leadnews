package com.aisile.controller.v1;


import com.aisile.article.AricleAuthorControllerApi;
import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.article.pojos.Article;
import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.service.IApAuthorService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * APP文章作者信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@RestController
@RequestMapping("/api/article/v1/apauthor")
public class ApAuthorController implements AricleAuthorControllerApi {

    @Autowired
    private IApAuthorService apAuthorService;

    @Override
    @PostMapping("/add")
    @GlobalTransactional(rollbackFor = Exception.class)
    public ApAuthor saveArticle(@RequestBody ApAuthor apAuthor) {
        apAuthorService.save(apAuthor);
        return apAuthor;
    }

    @Override
    @GetMapping("find/{id}")
    public ApAuthor findByUserId(@PathVariable("id") int user_id) {
        return apAuthorService.getById(user_id);
    }

}
