package com.aisile.service.impl;

import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.AdChannelMapper;
import com.aisile.model.admin.dtos.ChannelDto;
import com.aisile.model.admin.pojos.AdChannel;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.service.IAdChannelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 频道信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-10-27
 */
@Service
public class AdChannelServiceImpl extends ServiceImpl<AdChannelMapper, AdChannel> implements IAdChannelService {

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Override
    public ResponseResult<Object> showAllChannel(ChannelDto dto) {
        dto.checkParam();
        LambdaQueryWrapper<AdChannel> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(dto.getName() != null && !"".equals(dto.getName()),
                AdChannel::getName, dto.getName());
        IPage<AdChannel> page1 = adChannelMapper.selectPage(
                new Page<>(dto.getPage(),
                        dto.getSize()), wrapper);
        ResponseResult result = new PageResponseResult((int) page1.getCurrent(),
                (int) page1.getSize(), (int) page1.getTotal());
        result.setData(page1.getRecords());
        return result;
    }

    @Override
    public ResponseResult<Object> addAdChannel(AdChannel adChannel) {
        if (adChannel==null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_INVALID);
        int insert = adChannelMapper.insert(adChannel);
        ResponseResult result;
        if (insert > 0) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
        } else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        return result;
    }
}
