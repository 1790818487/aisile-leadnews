package com.aisile.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class OpenFeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = "";
        token = attributes.getRequest().getHeader("token");
        requestTemplate.header("token", token);
    }
}
