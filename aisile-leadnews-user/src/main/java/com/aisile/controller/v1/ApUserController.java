package com.aisile.controller.v1;


import com.aisile.user.ApUserControllerApi;
import com.aisile.utils.file.OSSClientUtil;
import com.aliyun.oss.internal.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * APP用户信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@RestController
@RequestMapping("/api/v1/apUser")
public class ApUserController implements ApUserControllerApi {

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Override
    @PostMapping("upload")
    public String uploadFile(@RequestPart MultipartFile file){
        String s = ossClientUtil.uploadFile(file);
        return s;
    }
}
