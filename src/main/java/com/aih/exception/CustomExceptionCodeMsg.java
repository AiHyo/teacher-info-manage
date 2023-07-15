package com.aih.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 此枚举类中定义的都是跟业务有关的异常
 */
@AllArgsConstructor
public enum CustomExceptionCodeMsg {
    INVALID_CODE(10000,"验证码无效"),
    USERNAME_NOT_EXISTS(10001,"用户名不存在"),
    USER_CREDIT_NOT_ENOUTH(10002,"用户积分不足"),
    USERNAME_OR_PASSWORD_ERROR(1001,"用户名或者密码错误"),
    SAVE_TEACHER_ERROR(1003,"新增教师用户失败"),
    TOKEN_INVALID(1004, "无效token"),

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
