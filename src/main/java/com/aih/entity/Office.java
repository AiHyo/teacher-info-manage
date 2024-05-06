package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 教研室
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@ApiModel(value = "Office对象", description = "教研室")
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教研室id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("教研室名称")
    @TableField("zw_office_name")
    private String officeName;

    @ApiModelProperty("所属学院id")
    @TableField("zw_cid")
    private Long cid;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;

}
