package com.aisile.common.exception;

import com.aisile.model.common.enums.AppHttpCodeEnum;

/**
 * @Auther:yry
 * @Date:2023/10/30 0030
 * @VERSON:1.0
 */
public class CustomExceptionCatch {

    public static void catchsApp(AppHttpCodeEnum appHttpCodeEnum){
        throw  new CustomException(appHttpCodeEnum.getCode(),appHttpCodeEnum.getErrorMessage());
    }
//    public static void catchsAdmin(AdminHttpCodeEnum appHttpCodeEnum){
//        throw  new CustomException(appHttpCodeEnum.getCode(),appHttpCodeEnum.getErrorMessage());
//    }
//    public static void catchsWemedia(WemediaHttpCodeEnum appHttpCodeEnum){
//        throw  new CustomException(appHttpCodeEnum.getCode(),appHttpCodeEnum.getErrorMessage());
//    }

}
