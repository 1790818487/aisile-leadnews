package com.aisile.jobs;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.utils.common.AppJwtUtil;
import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Component
public class AutoPublishArticle {

    @Autowired
    private RestTemplate restTemplate;

    @XxlJob("articlePublishJob")
    public ReturnT<String> publishArticle(String param) throws Exception {
        System.out.println(param);
        List<WmNews> list = new ArrayList<>();
        /*
        远程调用,按条件查询出需要的数据,判断是否到发布的时间了
         */
        //远程调用需要使用token验证才能通过，直接生成一个token，来通过拦截器
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("token", AppJwtUtil.getToken(System.currentTimeMillis()));
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<Object> entity = new HttpEntity<>(null, httpHeaders);
        List list_wmnews = restTemplate.getForObject("http://127.0.0.1:8004/api/wmnews/v1/showPublish", List.class, entity);

        List<WmNews> wmNews = new ArrayList<>();
        if (list_wmnews != null)
            for (Object wmnew : list_wmnews) {
                WmNews wmNews1 = JSON.parseObject(wmnew.toString(), WmNews.class);
                wmNews.add(wmNews1);
            }
        //遍历得到的数据，
        wmNews.forEach(a -> {
            if (a.getPublishTime().getTime() < System.currentTimeMillis())
                list.add(a);
        });
        //封装头信息和请求体进行数据发送
        HttpEntity<Object> entity2 = new HttpEntity<>(list, httpHeaders);

        //通过远程调用实现对到了时间需要发布的文章库做保存,只有集合中存在数据才执行
        if (list.size() != 0) {
            restTemplate.postForObject("http://127.0.0.1:8003/api/article/v1/article/save-publish",
                    entity2, ResponseResult.class);
        }
        return ReturnT.SUCCESS;
    }

}
