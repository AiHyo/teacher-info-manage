package com.aih.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Long id;

    @ApiModelProperty("教师id")
    private Long tid;

    @ApiModelProperty("职务id")
    private Long rid;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    private Integer deleted;

}
