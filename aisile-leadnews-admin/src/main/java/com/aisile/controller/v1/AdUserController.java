package com.aisile.controller.v1;

import com.aisile.admin.AdminLoginControllerApi;
import com.aisile.model.admin.dtos.AdUserDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.service.IAdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理员用户信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-10-31
 */
@RestController
@RequestMapping("/api/v1/login")
public class AdUserController implements AdminLoginControllerApi {

    @Autowired
    private IAdUserService adUserService;

    @Override
    @GetMapping("in")
    public ResponseResult login(@RequestBody AdUserDto dto) {
        return adUserService.login(dto);
    }
}
