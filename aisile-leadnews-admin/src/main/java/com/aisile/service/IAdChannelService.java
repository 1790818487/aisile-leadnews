package com.aisile.service;

import com.aisile.model.admin.dtos.ChannelDto;
import com.aisile.model.admin.pojos.AdChannel;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 频道信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-10-27
 */
public interface IAdChannelService extends IService<AdChannel> {

    /**
     * 返回分页查询结果集
     *
     * @return: com.aisile.model.common.dtos.ResponseResult<java.lang.Object>
     */
    ResponseResult<Object> showAllChannel(ChannelDto dto);

    ResponseResult<Object> addAdChannel(AdChannel adChannel);

}
