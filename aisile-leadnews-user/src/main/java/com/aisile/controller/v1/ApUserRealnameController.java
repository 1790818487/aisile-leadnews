package com.aisile.controller.v1;


import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.user.pojos.ApUserRealname;
import com.aisile.service.IApUserRealnameService;
import com.aisile.user.UserRealNameControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;

/**
 * <p>
 * APP实名认证信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/api/user/ApUserRealname")
public class ApUserRealnameController implements UserRealNameControllerApi {

    @Autowired
    private IApUserRealnameService userRealnameService;

    @Override
    @PostMapping("{status}")
    public ResponseResult showRealName(@RequestBody PageRequestDto dto,@PathVariable int status) {
        return userRealnameService.showRealName(dto,status);
    }

    @Override
    @PutMapping("{id}/{status}")
    public ResponseResult updateStatus(@PathVariable int status,@PathVariable int id) {
        return userRealnameService.updateStatus(status,id);
    }

}
