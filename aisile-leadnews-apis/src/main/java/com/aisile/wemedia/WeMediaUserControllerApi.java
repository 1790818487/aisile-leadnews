package com.aisile.wemedia;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmUser;

public interface WeMediaUserControllerApi {
    ResponseResult addWmUser(WmUser wmUser);

    WmUser findByUserId(int user_id);
    ResponseResult updateById(WmUser wmUser);
}