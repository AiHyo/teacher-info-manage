package com.aih.common;

import com.aih.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> success(T data){
        return new R<>(200, "success", data);
    }
    public static <T> R<T> success(){
        return new R<>(200, "success", null);
    }
    public static <T> R<T> success(String msg,T data){
        return new R<>(200,msg, data);
    }


    public static <T> R<T> error(CustomException customException){
        return  new R<>(customException.getCode(), customException.getMsg(), null);
    }
    public static <T> R<T> error(Integer code,String msg){
        return new R<>(code,msg, null);
    }

    public static <T> R<T> error(String msg){
        return new R<>(500,msg, null);
    }

}
