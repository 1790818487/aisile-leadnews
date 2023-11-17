package com.aisile.service.impl;


import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.SensitiveMapper;
import com.aisile.model.admin.dtos.SensitiveDto;
import com.aisile.model.admin.pojos.AdSensitive;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.service.ISensitiveService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        wrapper.like(!"".equals(dto.getName()), AdSensitive::getSensitives, dto.getName());
        Page<AdSensitive> page = new Page<>(dto.getPage(), dto.getSize());
        IPage<AdSensitive> page1 = this.page(page, wrapper);
        ResponseResult result = new PageResponseResult((int) page1.getCurrent(), (int) page1.getSize(), (int) page1.getTotal());
        result.setData(page1.getRecords());
        return result;
    }

    @Override
    public ResponseResult addSensitive(String name) {
        if (name == null || "".equals(name.trim()))
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_REQUIRE);
        AdSensitive adSensitive = new AdSensitive();
        adSensitive.setSensitives(name);
        adSensitive.setCreatedTime(new Date());
        LambdaQueryWrapper<AdSensitive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdSensitive::getSensitives, name);
        AdSensitive one = this.getOne(wrapper);
        if (one == null) {
            this.save(adSensitive);
            return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
        } else
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
    }

    @Override
    public ResponseResult delSensitive(int id) {

        Wrappers.lambdaQuery(new AdSensitive()).eq(AdSensitive::getId,id);

        boolean b = this.removeById(id);
        if (b)
            return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
        else
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_ALREADY_DEL);
    }
}
