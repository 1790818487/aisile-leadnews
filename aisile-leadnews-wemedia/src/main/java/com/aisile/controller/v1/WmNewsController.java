package com.aisile.controller.v1;


import com.aisile.model.article.pojos.Article;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmNewsDto;
import com.aisile.model.media.dtos.WmNewsPageReqDto;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.service.IWmNewsService;
import com.aisile.wemedia.WmNewsControllerApi;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 自媒体图文内容信息表 前端控制器
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@RestController
@RequestMapping("/api/wmnews/v1")
public class WmNewsController implements WmNewsControllerApi {
    @Autowired
    private IWmNewsService iWmNewsService;

    @Override
    @PostMapping("context")
    public ResponseResult showAllContext(@RequestBody WmNewsPageReqDto dto) {
        return iWmNewsService.showAllContext(dto);
    }

    /**
     * 自媒体用户发布文章,进行保存和审核
     * @param dto
     *
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    @Override
    @PostMapping("add-wm-news")
    public ResponseResult addNews(@RequestBody WmNewsDto dto) {
        return iWmNewsService.addNews(dto);
    }

    @Override
    public ResponseResult findById(Integer id) {
        return null;
    }

    @Override
    @GetMapping("/findone/{id}")
    public WmNews findByIdToFeign(@PathVariable Integer id) {
        return iWmNewsService.getById(id);
    }

    @Override
    @PostMapping("/update")
    public WmNews updateWmNewsFroFeign(@RequestBody WmNews wmNews) {
        iWmNewsService.updateById(wmNews);
        return wmNews;
    }

    /**
     *被feign调用来做定时任务的
     * @return: java.util.List<com.aisile.model.article.pojos.Article>
    */
    @Override
    @GetMapping("showPublish")
    public List<WmNews> showAllPublishtime() {
        return new LambdaQueryChainWrapper<>(iWmNewsService.getBaseMapper())
                .eq(WmNews::getStatus,8)
                .ge(WmNews::getPublishTime,System.currentTimeMillis()-1000*65)
                .ne(WmNews::getPublishTime,null)
                .list();
    }

    /**
     * @param id wmnews的id.用来进行文章的删除
     *
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    @Override
    @DeleteMapping("delWmNews/{id}")
    public ResponseResult delWmNewsById(@PathVariable int id) {
        return iWmNewsService.delWmNewsById(id);
    }
}
