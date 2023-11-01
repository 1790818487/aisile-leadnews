package com.aisile.common.exception;

import com.aisile.model.common.dtos.ResponseResult;

public class CustomException extends RuntimeException{

    private ResponseResult responseResult;

    public CustomException(Integer code, String errorMsg) {
        responseResult = new ResponseResult(code,errorMsg);
    }

    public ResponseResult getResponseResult() {
        return responseResult;
    }
}
