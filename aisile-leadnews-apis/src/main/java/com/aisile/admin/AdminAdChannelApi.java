package com.aisile.admin;

import com.aisile.model.admin.dtos.ChannelDto;
import com.aisile.model.admin.pojos.AdChannel;
import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminAdChannelApi {

    ResponseResult<Object> showAllChannel(ChannelDto dto);

    ResponseResult<Object> addAllChannel(@RequestBody AdChannel adChannel);

    ResponseResult showAllChannelNoPage();
}
