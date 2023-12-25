package com.aih.common.exception;

import lombok.AllArgsConstructor;

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
    HAS_DELETE(1008,"非法删除：已经删除过的数据,不允许再次删除"),
    NO_POWER_DELETE(1009,"删除失败,您没有权限进行该操作"),
    NO_POWER_QUERY(1010,"权限不足,查询失败,您没有权力查询该信息" ),
    NO_POWER_AUDIT(1011,"权限不足,审核失败,您没有权力审核该信息" ),
    NULL_PASSWORD(1012,"密码不能为空"),
    USER_IS_NOT_SELF(1023,"非法操作,不是本人,请操作自己的信息"),
    USER_IS_NOT_TEACHER(1013, "非法操作,当前用户没有教师权限"),
    USER_IS_NOT_AUDITOR(1014, "非法操作,当前用户没有审核员权限"),
    USER_IS_NOT_ADMIN(1014, "非法操作,当前用户没有管理员权限"),
    USER_IS_NOT_SUPERADMIN(1015, "非法操作,当前用户不是超级管理员"),
    UPDATE_AUDIT_POWER_ERROR(1016,"修改失败,请确认修改教师都属于您管理的学院"),
    IDS_ILLEGAL(1017,"传入ids非法,不存在其中id,请你重新确认"),
    USER_IS_DISABLED(1018,"账号已经被禁用"),
    FILE_NOT_EXIST(1019,"文件不存在"),
    PATH_PARAM_ILLEGAL(1020,"路径参数非法"),
    NOT_FOUND_TEACHER(1021, "未找到教师"),
    MISSING_PARAMETER(1022,"缺少参数"),
    POWER_NOT_MATCH(1030,"当前用户没有权限进行该操作"),
    NOT_FOUND_OFFICE(1031, "未找到教研室"),
    NOT_FOUND_COLLEGE(1032, "未找到该学院"),

    ID_IS_NULL(1040, "id不能为空"),
    ID_NOT_EXIST(1041, "id不存在,未找到对应数据"),
    OLD_PASSWORD_ERROR(1050, "原密码输入错误"),
    STATUS_ILLEGAL(1060, "status参数非法,只能允许0或者1"),
    ISAUDITOR_ILLEGAL(1061, "isAuditor参数非法,只能允许0或者1"),
    AUDIT_STATUS_ILLEGAL(1062, "auditStatus参数非法,只能允许0或者1或者2"),
    CID_ILLEGAL(1063, "cid非法,请确认学院id是否存在"),
    COLLEGE_DELETE_ERROR(1070, "学院删除失败,有教师隶属该学院"),
    OFFICE_DELETE_ERROR(1071, "教研室删除失败,有教师隶属该办公室"),

    FILE_NOT_FOUND(2001, "系统找不到指定路径"),
    ILLEGAL_ARGUMENT(2002, "非法参数异常"),
    PARAM_FORMAT_ERROR(2003, "参数格式错误"),
    NULL_POINTER(2004, "未知空指针异常"),
    BAD_SQL_GRAMMAR(2005, "SQL语法错误异常"),
    MISSING_SERVLET_REQUEST_PARAMETER(2006, "缺少请求参数异常"),
    EXCEL_IMPORT_ERROR(2007, "Excel导入失败"),
    DATA_EXPORT_ERROR(2008, "数据导出失败"),
    REQUEST_METHOD_NOT_MATCH(2009,"请求方式与Content-Type不匹配"),
    ZIP_DOWNLOAD_ERROR(2010,"下载zip压缩包过程发生异常"),
    ZIP_DOWNLOAD_CLOSE_ERROR(2011,"下载zip压缩包时,关流失败"),
    UNKNOWN_HOST(2020,"未知主机异常,ip接口网址失效"),
    SQL_NON_TRANSIENT_CONNECTION_EXCEPTION(2021, "数据库连接异常"),
    UTIL_EXCEPTION(2030, "工具类异常"),
    IO_EXCEPTION(2040, "IO异常"),
    REQUEST_CHANGE_COLLEGE_ERROR_TID_IS_SELF(1080, "申请变更学院失败,不能变更自己的学院"),
    REQUEST_CHANGE_COLLEGE_ERROR_TID_IS_AUDITING(1081, "申请变更学院失败,该教师已经正在申请转学院"),
    NOT_FOUND_REQUEST_COLLEGE_CHANGE(1082, "未找到该申请记录"),
    AUDIT_ERROR_NOT_UNAUDIT(1083, "操作失败,该申请已经审核过了"),
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
