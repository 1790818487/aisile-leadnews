package com.aisile.common.filters.web;

import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.utils.common.AppJwtUtil;
import com.aisile.utils.threadlocal.AdminThreadLocalUtils;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(filterName = "AuthoriWebFilter", urlPatterns = "/*")
public class AuthoriWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //编码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if (request.getRequestURI().contains("/login/in"))
            filterChain.doFilter(request, response);
        String token = request.getHeader("token");
        ResponseResult result = null;
        try {
            if (token == null || "".equals(token.trim()))
                result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_REQUIRE);
            else {
                Claims claimsBody = AppJwtUtil.getClaimsBody(token);
                int i = AppJwtUtil.verifyToken(claimsBody);
                if (i == -1 || i == 0) {
                    if (i == 0)
                        response.setHeader("REF_TOKEN",
                                AppJwtUtil.getToken((long) AdminThreadLocalUtils.getUser().getId()));
                    filterChain.doFilter(request, response);
                } else if (i == 1)
                    result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRE);
                else if (i == 2)
                    result = ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_INVALID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result != null)
                response.getWriter().write(JSON.toJSONString(result));
        }

    }
}
