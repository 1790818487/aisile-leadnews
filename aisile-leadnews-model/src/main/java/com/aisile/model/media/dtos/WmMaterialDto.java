package com.aisile.model.media.dtos;

import com.aisile.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class WmMaterialDto extends PageRequestDto {
    /**
     * 是否收藏
     *  1：收藏
     */
    private Short isCollection;
}
