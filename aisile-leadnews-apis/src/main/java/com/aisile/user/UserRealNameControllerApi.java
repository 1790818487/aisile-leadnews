package com.aisile.user;

import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.user.pojos.ApUserFollow;
import com.aisile.model.user.pojos.ApUserRealname;


public interface UserRealNameControllerApi {
    ResponseResult showRealName(PageRequestDto dto,int status);
    ResponseResult updateStatus(int status,int id);
    ResponseResult userUealName(ApUserRealname realname);
}
