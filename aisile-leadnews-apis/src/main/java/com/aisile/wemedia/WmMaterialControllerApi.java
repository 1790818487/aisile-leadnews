package com.aisile.wemedia;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmMaterialDto;
import com.aisile.model.media.dtos.WmNewsDto;
import com.aisile.model.media.pojos.WmNews;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialControllerApi {

   ResponseResult showAllMaterial(WmMaterialDto dto);

   ResponseResult getAllChannel();

   //图片上传
   ResponseResult addMaterial(@RequestPart MultipartFile file);

   //删除素材
   ResponseResult delMaterialById(@PathVariable int id);
}
