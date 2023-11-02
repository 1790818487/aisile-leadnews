package com.aisile.service;

import com.aisile.model.admin.dtos.SensitiveDto;
import com.aisile.model.admin.pojos.AdSensitive;

import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 敏感词信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-01
 */
public interface ISensitiveService extends IService<AdSensitive> {

    ResponseResult showAllSensitive(SensitiveDto dto);
}
