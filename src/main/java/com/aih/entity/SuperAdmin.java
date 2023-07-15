package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("超级管理员名称")
    private String superadminName;

    @ApiModelProperty("登录账号")
    private String username;

    @ApiModelProperty("登陆密码")
    private String pwd;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;


}
