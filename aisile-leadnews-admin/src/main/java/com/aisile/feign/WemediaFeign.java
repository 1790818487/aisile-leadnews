package com.aisile.feign;

import com.aisile.model.media.pojos.WmNews;
import com.aisile.model.media.pojos.WmUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
@FeignClient("wemedia-service")
public interface WemediaFeign {

    @GetMapping("/api/wmuser/fing/{id}")
    public WmUser findByUserId(@PathVariable("id") Integer userId);

    @GetMapping("/api/wmnews/v1/findone/{id}")
    public WmNews findByIdToFeign(@PathVariable("id") Integer id);

    @PostMapping("/api/wmnews/v1/update")
    public WmNews updateWmNewsFroFeign(@RequestBody WmNews wmNews);

    @GetMapping("/api/wmnews/v1/showPublish")
    public List<WmNews> showAllPublishtime();
}
