package com.aisile.feign;

import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.HeaderParam;

@FeignClient("wemedia-service")
public interface WemediaOpenFeign {

    @PostMapping(value = "/api/wmuser/add/wemedia")
    public ApAuthor addWmUser(@RequestBody WmUser wmUser);

    @GetMapping(value = "/api/wmuser/fing/{id}")
    public WmUser findByUserId(@PathVariable("id") int user_id);

    @PostMapping(value = "/api/wmuser/update")
    public ResponseResult updateById(@RequestBody WmUser wmUser);
}
