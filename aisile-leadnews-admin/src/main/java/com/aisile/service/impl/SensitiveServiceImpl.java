package com.aisile.service.impl;


import com.aisile.mapper.SensitiveMapper;
import com.aisile.model.admin.dtos.SensitiveDto;
import com.aisile.model.admin.pojos.AdSensitive;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.service.ISensitiveService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 敏感词信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-01
 */
@Service
public class SensitiveServiceImpl extends ServiceImpl<SensitiveMapper, AdSensitive> implements ISensitiveService {

    @Override
    public ResponseResult showAllSensitive(SensitiveDto dto) {
        dto.checkParam();
        LambdaQueryWrapper<AdSensitive> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!"".equals(dto.getName()),AdSensitive::getSensitives,dto.getName());
        Page<AdSensitive> page = new Page<>(dto.getPage(), dto.getSize());
        IPage<AdSensitive> page1 = this.page(page, wrapper);
        ResponseResult result = new PageResponseResult((int)page1.getCurrent(),(int)page1.getSize(),(int)page1.getTotal());
        result.setData(page1.getRecords());
        return result;
    }
}
