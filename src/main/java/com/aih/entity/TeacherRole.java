package com.aih.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
@Data
@TableName("teacher_role")
@ApiModel(value = "TeacherRole对象", description = "")
public class TeacherRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色_职务_关系id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("教师id")
    @TableField("zw_tid")
    private Long tid;

    @ApiModelProperty("职务id")
    @TableField("zw_rid")
    private Long rid;

    public TeacherRole(Long tid, Long rid) {
        this.tid = tid;
        this.rid = rid;
    }

//    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
//    @TableLogic
//    private Integer deleted;

}
