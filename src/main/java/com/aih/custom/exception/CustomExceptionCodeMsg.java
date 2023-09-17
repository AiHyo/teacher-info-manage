package com.aih.custom.exception;

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
    AUDIT_PASS_OR_REJECT(1007,"审核已通过或已驳回，无法删除"),
    HAS_DELETE(1008,"非法删除：已经删除过的记录,不允许再次删除"),
    NO_POWER_DELETE(1009,"权限不足,删除失败,请确认登录用户"),
    NO_POWER_QUERY(1010,"权限不足,查询失败,请确认登录用户" ),
    NO_POWER_AUDIT(1011,"权限不足,审核失败,请确认登录用户" ),
    NULL_PASSWORD(1012,"密码不能为空"),
    USER_IS_NOT_SELF(1023,"非法操作,当前用户不是本人"),
    USER_IS_NOT_TEACHER(1013, "非法操作,当前用户不是教师"),
    USER_IS_NOT_AUDITOR(1014, "非法操作,当前用户不是审核员"),
    USER_IS_NOT_ADMIN(1014, "非法操作,当前用户不是管理员"),
    USER_IS_NOT_SUPERADMIN(1015, "非法操作,当前用户不是超级管理员"),
    UPDATE_AUDIT_POWER_ERROR(1016,"修改审核权限失败,请确认修改教师都属于您管理的学院"),
    IDS_ILLEGAL(1017,"传入ids非法,不存在其中id,请你重新确认"),
    USER_IS_DISABLED(1018,"账号已经被禁用"),
    FILE_NOT_EXIST(1019,"文件不存在"),
    PATH_PARAM_ILLEGAL(1020,"路径参数非法"),
    NOT_FOUND_TEACHER(1021, "未找到教师"),
    MISSING_PARAMETER(1022,"缺少参数"),
    POWER_NOT_MATCH(1030,"权限不匹配,请确认登录用户"),
    NOT_FOUND_OFFICE(1031, "未找到教研室"),

    FILE_NOT_FOUND(2001, "系统找不到指定路径"),
    ILLEGAL_ARGUMENT(2002, "非法参数异常"),
    PARAM_FORMAT_ERROR(2003, "参数格式错误"),
    NULL_POINTER(2004, "未知空指针异常"),
    BAD_SQL_GRAMMAR(2005, "SQL语法错误异常"),
    MISSING_SERVLET_REQUEST_PARAMETER(2006, "缺少请求参数异常"),
    EXCEL_IMPORT_ERROR(2007, "Excel导入失败"),
    DATA_EXPORT_ERROR(2008, "数据导出失败"),
    //添加一个请求方式与Content-Type不匹配,
    REQUEST_METHOD_NOT_MATCH(2009,"请求方式与Content-Type不匹配"),
    ZIP_DOWNLOAD_ERROR(2010,"下载zip压缩包过程发生异常"),
    //添加一个zip包下载关流失败,
    ZIP_DOWNLOAD_CLOSE_ERROR(2011,"下载zip压缩包时,关流失败");





    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
