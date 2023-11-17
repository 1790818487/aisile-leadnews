package com.aisile.wemedia;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmUserDto;
import com.aisile.model.media.pojos.WmUser;

public interface WeMediaUserControllerApi {
    WmUser addWmUser(WmUser wmUser);

    WmUser findByUserId(int user_id);

    ResponseResult updateById(WmUser wmUser);

    ResponseResult loginWeUser(WmUserDto wmUserDto);

}
