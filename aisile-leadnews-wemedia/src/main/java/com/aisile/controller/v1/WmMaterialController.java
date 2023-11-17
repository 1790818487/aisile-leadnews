package com.aisile.controller.v1;

import com.aisile.feign.AdChannelFeign;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.dtos.WmMaterialDto;
import com.aisile.model.media.pojos.WmMaterial;
import com.aisile.service.IWmMaterialService;
import com.aisile.utils.file.OSSClientUtil;
import com.aisile.utils.threadlocal.WmThreadLocalUtils;
import com.aisile.wemedia.WmMaterialControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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

    @Autowired
    private AdChannelFeign adChannelFeign;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Override
    @PostMapping("show")
    public ResponseResult showAllMaterial(@RequestBody WmMaterialDto dto) {
        return iWmMaterialService.showAllMaterial(dto);
    }

    @Override
    @GetMapping("channel")
    public ResponseResult getAllChannel() {
        return adChannelFeign.showAllChannelNoPage();
    }

    @PostMapping("uploadImages")
    @Override
    public ResponseResult addMaterial(@RequestPart MultipartFile file) {
        String s = ossClientUtil.uploadFile(file);
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUrl(s);
        wmMaterial.setUserId(WmThreadLocalUtils.getUser().getApUserId());
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setType((short) 0);
        iWmMaterialService.save(wmMaterial);
        return ResponseResult.okResult(s);
    }

    @Override
    @DeleteMapping("{id}")
    public ResponseResult delMaterialById(@PathVariable int id) {
        return null;
    }


}
