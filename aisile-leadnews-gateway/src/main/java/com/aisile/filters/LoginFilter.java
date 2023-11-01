package com.aisile.filters;

import com.aisile.utils.AppJwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoginFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //对登录的接口放行
        if (request.getPath().toString().contains("/login/in"))
            return chain.filter(exchange);
        else {
            String token = request.getHeaders().getFirst("token");
            //和前端进行约定响应头传token，判断空
            if (token == null || "".equals(token.trim())) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            try {
                AppJwtUtil.verifyToken(AppJwtUtil.getClaimsBody(token));
                //验证token的正确性
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

        }
        return chain.filter(exchange);
    }
}
