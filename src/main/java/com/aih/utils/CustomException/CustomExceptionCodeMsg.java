package com.aih.utils.CustomException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 此枚举类中定义的都是跟业务有关的异常
 */
@AllArgsConstructor
public enum CustomExceptionCodeMsg {
    SERVER_ERROR(500, "服务器异常"),

    USERNAME_OR_PASSWORD_ERROR(1001,"用户名或者密码错误"),
    SAVE_TEACHER_ERROR(1002,"新增教师用户失败"),
    TOKEN_INVALID(1003, "无效token，请重新登录"),
    TOKEN_ILLEGAL(1004, "非法token，entityType异常"),
    PARAM_FORMAT_ERROR(1005, "参数格式错误"),
//    UPDATE_TEACHER_ERROR(1006, "修改教师基础信息失败"),
    AUDIT_PASS_OR_REJECT(1007,"审核已通过或已驳回，无法删除"),
    HAS_DELETE(1008,"非法删除：已经删除过的记录,不允许再次删除"),
    NO_POWER_DELETE(1009,"权限不足,删除失败,请确认登录用户"),
    NO_POWER_QUERY(1010,"权限不足,查询失败,请确认登录用户" ),
    NO_POWER_AUDIT(1011,"权限不足,审核失败,请确认登录用户" ),
    NULL_PASSWORD(1012,"密码不能为空"),
    USER_IS_NOT_TEACHER(1013, "非法操作,当前用户不是教师"),
    USER_IS_NOT_ADMIN(1014, "非法操作,当前用户不是管理员"),
    USER_IS_NOT_SUPERADMIN(1015, "非法操作,当前用户不是超级管理员"),
    //添加一个 修改审核权限失败,请确认修改教师都属于您管理的学院
    UPDATE_AUDIT_POWER_ERROR(1016,"修改审核权限失败,请确认修改教师都属于您管理的学院"),
    //添加一个 传入id非法,请确认传入的id是否正确
    IDS_ILLEGAL(1017,"传入ids非法,请确认传入的ids是否正确"),
    //添加一个 账号已经被禁用
    USER_IS_DISABLED(1018,"账号已经被禁用"),
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
