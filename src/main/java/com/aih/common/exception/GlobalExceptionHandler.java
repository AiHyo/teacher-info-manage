package com.aih.common.exception;

import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.utils.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = {RestController.class, Controller.class})   //这个注解表示这个类是全局异常处理类
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public <T> R<T> exceptionHandler(Exception e){
        //如果拦截到的Exception是自定义异常,则自定义异常处理
        if(e instanceof CustomException){
            //强转为自定义异常
            CustomException customException = (CustomException) e;
            //返回自定义异常
            return R.error(customException.getCode(),customException.getMsg());
        }
        log.error("【非自定义异常】↓↓↓",e);
        //不是自定义异常，则统一返回
        return R.error(CustomExceptionCodeMsg.SERVER_ERROR.getCode(),CustomExceptionCodeMsg.SERVER_ERROR.getMsg());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> R<T> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        throw new CustomException(CustomExceptionCodeMsg.PARAM_FORMAT_ERROR);
        return R.error(CustomExceptionCodeMsg.PARAM_FORMAT_ERROR.getCode(), CustomExceptionCodeMsg.PARAM_FORMAT_ERROR.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public <T> R<T> handleIllegalArgumentException(IllegalArgumentException ex) {
        return R.error(CustomExceptionCodeMsg.NULL_PASSWORD.getCode(), CustomExceptionCodeMsg.NULL_PASSWORD.getMsg());
    }

    /*@ExceptionHandler(value = {CustomException.class})
    public <T> R<T> handleCustomException(CustomException e) {
        return R.error(e.getCode(), e.getMsg());
    }
    @ExceptionHandler(value = {Exception.class})
    public <T> R<T> handleException(Exception e) {
        return R.error(500, "服务器端异常");
    }*/
}
