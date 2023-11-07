package com.aisile.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@MapperScan("com.aisile.mapper")
@ComponentScan({"com.aisile.common.exception","com.aisile.common.knife4j"})
@EnableDiscoveryClient
@EnableWebMvc
@ServletComponentScan("com.aisile.common.filters.web")
@EnableFeignClients(basePackages = "com.aisile.feign")
@EnableTransactionManagement
public class SpringBootConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
