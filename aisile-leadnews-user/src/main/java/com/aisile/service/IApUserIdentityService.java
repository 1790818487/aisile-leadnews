package com.aisile.service;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.user.dtos.AuthDto;
import com.aisile.model.user.pojos.ApUserIdentity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * APP身份认证信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
public interface IApUserIdentityService extends IService<ApUserIdentity> {

    ResponseResult userIdentity(AuthDto dto,short type);


}
