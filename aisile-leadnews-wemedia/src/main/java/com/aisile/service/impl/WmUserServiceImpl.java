package com.aisile.service.impl;

import com.aisile.model.media.pojos.WmUser;
import com.aisile.mapper.WmUserMapper;
import com.aisile.service.IWmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自媒体用户信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements IWmUserService {

}
