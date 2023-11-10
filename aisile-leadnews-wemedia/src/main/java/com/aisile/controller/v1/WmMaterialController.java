package com.aisile.controller.v1;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmMaterialDto;
import com.aisile.service.IWmMaterialService;
import com.aisile.wemedia.WmMaterialControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 自媒体图文素材信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-10
 */
@RestController
@RequestMapping("/api/material/v1")
public class WmMaterialController implements WmMaterialControllerApi {

    @Autowired
    private IWmMaterialService iWmMaterialService;


    @Override
    @PostMapping("show")
    public ResponseResult showAllMaterial(@RequestBody WmMaterialDto dto) {
        return iWmMaterialService.showAllMaterial(dto);
    }
}
