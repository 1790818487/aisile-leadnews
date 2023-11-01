package com.aisile.admin;


import com.aisile.model.admin.dtos.AdUserDto;
import com.aisile.model.common.dtos.ResponseResult;

/**
 * 单接口功能，只负责登录，如果有其他的操作。
 *
 * @return:
*/

public interface AdminLoginControllerApi {
    ResponseResult login(AdUserDto adUserDto);
}
