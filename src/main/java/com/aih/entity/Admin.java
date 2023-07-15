package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 管理员
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@ApiModel(value = "Admin对象", description = "管理员")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("管理员id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("管理员名称")
    private String adminName;

    @ApiModelProperty("登录账号")
    private String username;

    @ApiModelProperty("登录密码")
    private String pwd;

    @ApiModelProperty("所属学院id")
    private Long cid;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;

}
