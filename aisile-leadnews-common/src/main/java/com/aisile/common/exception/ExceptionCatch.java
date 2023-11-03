package com.aisile.common.exception;

import com.aisile.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.hbase.thirdparty.com.google.common.collect.ImmutableMap;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther:yry
 * @Date:2023/10/30 0030
 * @VERSON:1.0
 */
@Slf4j
@ControllerAdvice   // controller层 增强类  作用  环绕整个controller 所有controller执行都会被这个类抓取到
public class ExceptionCatch {

    public static ImmutableMap<Class<? extends Exception>, ResponseResult> exceptions;
    public static ImmutableMap.Builder<Class<? extends Exception>, ResponseResult> builder = ImmutableMap.builder();

    // exceptions =  builder.build();
    static {
        builder.put(NullPointerException.class, ResponseResult.errorResult(20001, "数据空啦"));
        builder.put(IndexOutOfBoundsException.class, ResponseResult.errorResult(20002, "下标超越限制了"));
        builder.put(ArithmeticException.class, ResponseResult.errorResult(20003, "被除数为0啦！"));
        builder.put(HttpMessageNotReadableException.class, ResponseResult.errorResult(20004, "请求参数没给！！"));
        builder.put(BadSqlGrammarException.class, ResponseResult.errorResult(20005, "sql语法错误！！"));
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseResult NotCustomException(Exception e) {
        e.printStackTrace();
        if (exceptions == null) {
            exceptions = builder.build();
        }
        ResponseResult responseResult = exceptions.get(e.getClass());
        if (responseResult == null) {
            return ResponseResult.errorResult(99999, "遇到不可预知异常啦，请联系管理员");
        }
        return responseResult;
    }

    // 自定义异常
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResponseResult CustomException(CustomException e) {
        return e.getResponseResult();
    }

}
