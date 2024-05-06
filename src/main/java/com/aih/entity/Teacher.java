package com.aih.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 教师(用户)
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Teacher对象", description = "教师(用户)")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Alias("教师id")
    @ApiModelProperty("教师id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("工位号")
    @TableField("zw_desk_id")
    private Long deskId;

    @Alias("教师姓名")
    @ApiModelProperty("姓名")
    @TableField("zw_teacher_name")
    private String teacherName;

//    @Alias("登录账号")
    @ApiModelProperty("登录账号")
    @TableField("zw_username")
    private String username;

//    @Alias("登陆密码")
    @ApiModelProperty("登录密码")
    @TableField("zw_password")
    private String password;

    @Alias("性别")
    @ApiModelProperty("性别 0:女 1:男")
    @TableField("zw_gender")
    private Integer gender;

    @Alias("民族")
    @ApiModelProperty("民族")
    @TableField("zw_ethnic")
    private String ethnic;

    @Alias("籍贯")
    @ApiModelProperty("籍贯")
    @TableField("zw_native_place")
    private String nativePlace;

    @Alias("住址")
    @ApiModelProperty("住址")
    @TableField("zw_address")
    private String address;

    @Alias("电话号码")
    @ApiModelProperty("电话号码")
    @TableField("zw_phone")
    private String phone;

    @Alias("学院")
    @ApiModelProperty("所属学院id")
    @TableField("zw_cid")
    private Long cid;

    @Alias("教研室")
    @ApiModelProperty("所属教研室id")
    @TableField("zw_oid")
    private Long oid;

    @Alias("政治面貌")
    @ApiModelProperty("政治面貌")
    @TableField("zw_politics_status")
    private String politicsStatus;

    @ApiModelProperty("文化程度")
    @TableField("zw_education_degree")
    private String educationDegree;

    @ApiModelProperty("身份证号")
    @TableField("zw_id_number")
    private String idNumber;

    @Alias("审核员")
    @ApiModelProperty("是否审核员 0:不是 1:是")
    @TableField("zw_is_auditor")
    private Integer isAuditor;

    @Alias("入校日期")
    @ApiModelProperty("入校日期")
    @TableField("zw_start_date")
    private LocalDate startDate;

    @Alias("权限生效日期")
    @ApiModelProperty("权限生效日期")
    @TableField(fill = FieldFill.INSERT, value = "zw_create_date")
    private LocalDate createDate;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

    @ApiModelProperty("备用3")
    private String test3;

    @ApiModelProperty("备用4")
    private String test4;

}
