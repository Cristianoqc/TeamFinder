package com.yupi.usercenterbackend.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    public static <T> BaseResponse success(T data){
        return new BaseResponse<>(0, data, "ok");
    }

    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int errorCode, String message, String description){
        return new BaseResponse<>(errorCode, null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description){
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String description){
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
