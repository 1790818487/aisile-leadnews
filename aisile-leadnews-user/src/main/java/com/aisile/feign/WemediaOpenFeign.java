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

    @PostMapping(value = "/api/wmuser/add/wemedia", headers = {"token:${token}"})
    public ApAuthor addWmUser(@RequestBody WmUser wmUser, @HeaderParam("token") String token);

    @GetMapping(value = "/api/wmuser/fing/{id}", headers = {"token:${token}"})
    public WmUser findByUserId(@PathVariable("id") int user_id, @HeaderParam("token") String token);

    @PostMapping(value = "/api/wmuser/update", headers = {"token:${token}"})
    public ResponseResult updateById(@RequestBody WmUser wmUser, @HeaderParam("token") String token);
}
