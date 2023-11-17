package com.aisile.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queen.article")
public class AutoPublishRabbitmq {

    @RabbitHandler
    public void PublishArticle(Object id) {
        System.out.println(id);
    }

}
