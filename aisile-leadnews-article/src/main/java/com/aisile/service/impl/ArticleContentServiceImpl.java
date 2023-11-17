package com.aisile.service.impl;

import com.aisile.model.article.pojos.ArticleContent;
import com.aisile.mapper.ArticleContentMapper;
import com.aisile.service.IArticleContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP已发布文章内容表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Service
public class ArticleContentServiceImpl extends ServiceImpl<ArticleContentMapper, ArticleContent> implements IArticleContentService {

}
