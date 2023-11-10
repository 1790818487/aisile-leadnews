package com.aisile.service;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmUserDto;
import com.aisile.model.media.pojos.WmUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体用户信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
public interface IWmUserService extends IService<WmUser> {
    ResponseResult loginWeUser(WmUserDto wmUserDto);
}
