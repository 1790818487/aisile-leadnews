package com.aisile.wemedia;

import com.aisile.model.article.pojos.Article;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmNewsDto;
import com.aisile.model.media.dtos.WmNewsPageReqDto;
import com.aisile.model.media.pojos.WmNews;

import java.util.List;

public interface WmNewsControllerApi {
    ResponseResult showAllContext(WmNewsPageReqDto dto);

    ResponseResult addNews(WmNewsDto dto);

    // 根据id查询文章信息
    public ResponseResult findById(Integer id);


    /**
     * 文章Id  feign调用使用
     * @param id
     * @return
     */
    public WmNews findByIdToFeign(Integer id);

    /**
     * 修改状态 feign调用
     * @param wmNews
     * @return
     */
    public WmNews updateWmNewsFroFeign(WmNews wmNews);


    /**
     *用于定时器执行定时发布的任务
     * @return: java.util.List<com.aisile.model.article.pojos.Article>
    */
    List<WmNews> showAllPublishtime();

    /**
     * @param id 传值wmnews的id,进行文章的删除
     *
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    ResponseResult delWmNewsById(int id);

}
