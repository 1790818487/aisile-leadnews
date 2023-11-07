package com.aisile.user;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.user.dtos.AuthDto;
import com.aisile.model.user.pojos.ApUserIdentity;

public interface UserAuthorIdentityControllerApi {
    ResponseResult submitMaterial(AuthDto dto,short type);
}
