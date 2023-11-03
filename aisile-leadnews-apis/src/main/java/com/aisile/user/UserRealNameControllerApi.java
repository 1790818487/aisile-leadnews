package com.aisile.user;

import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;


public interface UserRealNameControllerApi {
    ResponseResult showRealName(PageRequestDto dto,int status);
    ResponseResult updateStatus(int status,int id);
}
