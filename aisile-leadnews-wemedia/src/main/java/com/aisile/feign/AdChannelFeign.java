package com.aisile.feign;

import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("admin-service")
public interface AdChannelFeign {

    @GetMapping("/api/v1/adChannel")
    ResponseResult showAllChannelNoPage();
}
