package com.aih.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 超级管理员
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@TableName("super_admin")
@ApiModel(value = "SuperAdmin对象", description = "超级管理员")
public class SuperAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("超级管理员id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("超级管理员名称")
    @TableField("zw_superadmin_name")
    private String superadminName;

    @ApiModelProperty("登录账号")
    @TableField("zw_username")
    private String username;

    @ApiModelProperty("登陆密码")
    @TableField("zw_password")
    private String password;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;


}
