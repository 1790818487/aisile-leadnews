package com.aisile.feign;

import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface ApUserRealNameFeign {
    @PostMapping("/api/user/ApUserRealname/{status}")
    ResponseResult showRealName(@RequestBody PageRequestDto dto, @PathVariable int status);

    @PutMapping("/api/user/ApUserRealname/{id}/{status}")
    ResponseResult updateStatus(@PathVariable int status, @PathVariable int id);
}
