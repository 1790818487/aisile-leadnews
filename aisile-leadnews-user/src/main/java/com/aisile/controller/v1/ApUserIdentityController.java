package com.aisile.controller.v1;


import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.user.dtos.AuthDto;
import com.aisile.model.user.pojos.ApUserIdentity;
import com.aisile.user.UserAuthorIdentityControllerApi;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * APP身份认证信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@RestController
@RequestMapping("/api/identity")
public class ApUserIdentityController implements UserAuthorIdentityControllerApi {

    @Override
    @PostMapping("submit/{type}")
    public ResponseResult submitMaterial(@RequestBody AuthDto dto, @PathVariable short type) {

        return null;
    }
}
