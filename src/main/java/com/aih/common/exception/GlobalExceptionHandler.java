package com.aih.common.exception;

import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.utils.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})   //这个注解表示这个类是全局异常处理类
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public <T> R<T> exceptionHandler(Exception e){
        //如果拦截到的是自定义异常,则自己处理
        if(e instanceof CustomException){
            CustomException customException = (CustomException) e;//强转为自定义异常
            //返回自定义异常
            return R.error(customException.getCode(),customException.getMsg());
        }
        log.error("【非自定义异常】↓↓↓",e);
        //不是自定义异常,且未知,则统一返回500
        return R.error(CustomExceptionCodeMsg.SERVER_ERROR.getCode(),CustomExceptionCodeMsg.SERVER_ERROR.getMsg());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> R<T> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//        throw new CustomException(CustomExceptionCodeMsg.PARAM_FORMAT_ERROR);
        log.error("【参数格式错误】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.PARAM_FORMAT_ERROR.getCode(), CustomExceptionCodeMsg.PARAM_FORMAT_ERROR.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public <T> R<T> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("【非法参数】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.ILLEGAL_ARGUMENT.getCode(), CustomExceptionCodeMsg.ILLEGAL_ARGUMENT.getMsg());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public <T> R<T> handleFileNotFoundException(FileNotFoundException ex) {
        log.error("【文件未找到】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.FILE_NOT_FOUND.getCode(), CustomExceptionCodeMsg.FILE_NOT_FOUND.getMsg());
    }

    @ExceptionHandler(NullPointerException.class)
    public <T> R<T> handleNullPointerException(NullPointerException ex) {
        log.error("【空指针异常】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.NULL_POINTER.getCode(), CustomExceptionCodeMsg.NULL_POINTER.getMsg());
    }

    @ExceptionHandler(BadSqlGrammarException.class)//SQLSyntaxErrorException
    public <T> R<T> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("【SQL语法错误】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.BAD_SQL_GRAMMAR.getCode(), CustomExceptionCodeMsg.BAD_SQL_GRAMMAR.getMsg());
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public <T> R<T> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("【缺少请求参数】↓↓↓",ex);
        return R.error(CustomExceptionCodeMsg.MISSING_SERVLET_REQUEST_PARAMETER.getCode(), CustomExceptionCodeMsg.MISSING_SERVLET_REQUEST_PARAMETER.getMsg());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public <T> R<T> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("请求方式与Content-Type不匹配",ex);
        return R.error(CustomExceptionCodeMsg.REQUEST_METHOD_NOT_MATCH.getCode(),CustomExceptionCodeMsg.REQUEST_METHOD_NOT_MATCH.getMsg());
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
