package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("教师id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("姓名")
    private String teacherName;

    @ApiModelProperty("登录账号")
    private String username;

    @ApiModelProperty("登录密码")
    private String pwd;

    @ApiModelProperty("性别   0:女 1:男")
    private Integer gender;

    @ApiModelProperty("民族")
    private String ethnic;

    @ApiModelProperty("籍贯")
    private String birthplace;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("所属学院id")
    private Long cid;

    @ApiModelProperty("所属教研室id")
    private Long oid;

    @ApiModelProperty("是否审核员 0:不是 1:是")
    private Integer isAuditor;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
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
