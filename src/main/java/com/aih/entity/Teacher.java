package com.aih.entity;

import cn.hutool.core.annotation.Alias;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Alias("教师姓名")
    @ApiModelProperty("姓名")
    private String teacherName;

    @Alias("登录账号")
    @ApiModelProperty("登录账号")
    private String username;

    @Alias("登陆密码")
    @ApiModelProperty("登录密码")
    private String password;

    @Alias("性别")
    @ApiModelProperty("性别   0:女 1:男")
    private Integer gender;

    @Alias("民族")
    @ApiModelProperty("民族")
    private String ethnic;

    @Alias("籍贯")
    @ApiModelProperty("籍贯")
    private String birthplace;

    @Alias("住址")
    @ApiModelProperty("住址")
    private String address;

    @Alias("电话号码")
    @ApiModelProperty("电话号码")
    private String phone;

    @Alias("学院")
    @ApiModelProperty("所属学院id")
    private Long cid;

    @Alias("教研室")
    @ApiModelProperty("所属教研室id")
    private Long oid;

    @Alias("审核员")
    @ApiModelProperty("是否审核员 0:不是 1:是")
    private Integer isAuditor;

    @Alias("注册日期")
    @ApiModelProperty("生效日期")
    @TableField(fill = FieldFill.INSERT)
    private LocalDate createDate;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("备用1")
    private String test1;

    @ApiModelProperty("备用2")
    private String test2;

    @ApiModelProperty("备用3")
    private String test3;

    @ApiModelProperty("备用4")
    private String test4;

}
