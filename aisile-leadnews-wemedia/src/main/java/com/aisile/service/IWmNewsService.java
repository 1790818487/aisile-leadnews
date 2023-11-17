package com.aisile.service;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.dtos.WmNewsDto;
import com.aisile.model.media.dtos.WmNewsPageReqDto;
import com.aisile.model.media.pojos.WmNews;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * 自媒体图文内容信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
public interface IWmNewsService extends IService<WmNews> {
    ResponseResult showAllContext(WmNewsPageReqDto dto);

    /*
     * @param wmNews
     *  自媒体人上传图文内容
     * @return: com.aisile.model.common.dtos.ResponseResult
    */
    ResponseResult addNews(WmNewsDto dto);

    /**
     * @param id wmnews的id.用来进行文章的删除
     *
     * @return: com.aisile.model.common.dtos.ResponseResult
     */
    ResponseResult delWmNewsById(@PathVariable int id);
}
