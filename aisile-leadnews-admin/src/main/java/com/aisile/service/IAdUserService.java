package com.aisile.service;

import com.aisile.model.admin.dtos.AdUserDto;
import com.aisile.model.admin.pojos.AdUser;
import com.aisile.model.common.dtos.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 管理员用户信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-10-31
 */
public interface IAdUserService extends IService<AdUser> {

    ResponseResult login(AdUserDto dto);
}
