package com.aisile.service;

/**
 * @Auther:yry
 * @Date:2023/11/13 0013
 * @VERSON:1.0
 */
public interface WemediaNewsAutoScanService {

    /**
     * 自媒体文章审核
     * @param id 自媒体文章id  rabbitMQ 消息传递
     */
    public void autoScanByMediaNewsId(Integer id);
}
