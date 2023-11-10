package com.aisile.wemedia;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmMaterialDto;

public interface WmMaterialControllerApi {

   ResponseResult showAllMaterial(WmMaterialDto dto);
}
