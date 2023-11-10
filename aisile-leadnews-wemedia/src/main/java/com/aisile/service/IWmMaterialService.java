package com.aisile.service;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmMaterialDto;
import com.aisile.model.media.pojos.WmMaterial;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体图文素材信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-10
 */
public interface IWmMaterialService extends IService<WmMaterial> {
    ResponseResult showAllMaterial(WmMaterialDto dto);
}
