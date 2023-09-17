package com.aih.custom.exception;

public class CustomException extends RuntimeException{
    private Integer code = 500;
    private String msg = "服务器异常";
    public CustomException(Integer code,String msg){
        super();
        this.code = code;
        this.msg = msg;
    }

    public CustomException(CustomExceptionCodeMsg customExceptionCodeMsg){
        super();
        this.code = customExceptionCodeMsg.getCode();
        this.msg = customExceptionCodeMsg.getMsg();
    }

    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

}
