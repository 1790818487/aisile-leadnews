package com.aisile.model.media.dtos;

import com.aisile.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class WmNewsPageReqDto extends PageRequestDto {

    /**
     * 状态
     */
    private Short status;
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 频道
     */
    private Integer channelId;

    /**
     * 开始时间
     */
    private LocalDate beginPubDate;

    /**
     * 结束时间
     */
    private LocalDate endPubDate;

    public void checkParams(){
        if ("".equals(this.keyword))
            this.keyword=null;
    }

}
