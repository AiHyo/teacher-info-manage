package com.aih.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 身份证审核
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@TableName("identity_card_audit")
@ApiModel(value = "IdentityCardAudit对象", description = "身份证审核")
public class IdentityCardAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("身份证id")
    private Long id;

    @ApiModelProperty("所属教师的id")
    @TableField(fill = FieldFill.INSERT)
    private Long tid;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("附件路径")
    private String url;

    @ApiModelProperty("审核状态 0:待审核 1:通过 2:驳回")
    @TableField(fill = FieldFill.INSERT)
    private Integer auditStatus;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("审核时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime auditTime;

    @ApiModelProperty("审核员id")
    @TableField(fill = FieldFill.UPDATE)
    private Long aid;

    @ApiModelProperty("教师备注")
    private String teacherRemark;

    @ApiModelProperty("审核员备注")
    private String auditorRemark;

    @ApiModelProperty("是否展示 0:不展示 1:展示")
    @TableField(fill = FieldFill.INSERT)
    private Integer isShow;

    @ApiModelProperty("删除角色")
    @TableField(fill = FieldFill.INSERT)
    private String deleteRoles;


    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;

}
