package com.aisile.service;

import com.aisile.model.article.pojos.Article;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmNews;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
public interface IArticleService extends IService<Article> {

    ResponseResult saveArticle(List<WmNews> wmNews);

    ResponseResult delById(int id);
}
