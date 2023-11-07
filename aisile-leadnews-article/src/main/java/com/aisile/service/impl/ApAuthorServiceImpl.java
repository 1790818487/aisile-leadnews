package com.aisile.service.impl;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.mapper.ApAuthorMapper;
import com.aisile.service.IApAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP文章作者信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements IApAuthorService {

}
