package com.aisile.model.admin.dtos;

import com.aisile.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class NewsAuthDto extends PageRequestDto {

    /**
     * 标题
     */
    private String title;

    private Integer id;

    /**
     * 审核失败原因
     */
    private String msg;
}
