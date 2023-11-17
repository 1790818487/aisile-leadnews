package com.aisile.service.impl;

import com.aisile.model.article.pojos.ArticleConfig;
import com.aisile.mapper.ArticleConfigMapper;
import com.aisile.service.IArticleConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP已发布文章配置表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Service
public class ArticleConfigServiceImpl extends ServiceImpl<ArticleConfigMapper, ArticleConfig> implements IArticleConfigService {

}
