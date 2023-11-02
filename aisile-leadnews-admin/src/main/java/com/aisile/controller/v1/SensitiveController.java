package com.aisile.controller.v1;


import com.aisile.admin.AdminSensitiveApi;
import com.aisile.model.admin.dtos.SensitiveDto;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.service.ISensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 敏感词信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-01
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class SensitiveController implements AdminSensitiveApi {

    @Autowired
    private ISensitiveService sensitiveService;

    @Override
    @PostMapping("sensitive")
    public ResponseResult showAllSensitive(@RequestBody SensitiveDto dto) {
        return sensitiveService.showAllSensitive(dto);
    }
}
