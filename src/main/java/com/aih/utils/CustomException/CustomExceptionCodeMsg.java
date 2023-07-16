package com.aih.utils.CustomException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 此枚举类中定义的都是跟业务有关的异常
 */
@AllArgsConstructor
public enum CustomExceptionCodeMsg {
    USERNAME_OR_PASSWORD_ERROR(1001,"用户名或者密码错误"),
    SAVE_TEACHER_ERROR(1002,"新增教师用户失败"),
    TOKEN_INVALID(1003, "无效token,请重新登录"),
    PARAM_FORMAT_ERROR(1004, "参数格式错误"),
    SERVER_ERROR(500, "服务器异常"),

    ;


    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
