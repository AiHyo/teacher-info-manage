package com.aih.entity.audit;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 教育经历审核
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@TableName("education_experience_audit")
@ApiModel(value = "EducationExperienceAudit对象", description = "教育经历审核")
public class EducationExperienceAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教育经历审核id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属的教师id")
    @TableField(fill = FieldFill.INSERT, value = "zw_tid")
    private Long tid;

    @ApiModelProperty("开始日期")
    @TableField("zw_sta_date")
    private LocalDate staDate;

    @ApiModelProperty("结束日期")
    @TableField("zw_end_date")
    private LocalDate endDate;

    @ApiModelProperty("学校")
    @TableField("zw_school")
    private String school;

    @ApiModelProperty("专业")
    @TableField("zw_major")
    private String major;

    @ApiModelProperty("审核状态 0:待审核 1:通过 2:驳回")
    @TableField(fill = FieldFill.INSERT, value = "zw_audit_status")
    private Integer auditStatus;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT, value = "zw_create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("审核时间")
    @TableField(fill = FieldFill.UPDATE, value = "zw_audit_time")
    private LocalDateTime auditTime;

    @ApiModelProperty("审核员id")
    @TableField(fill = FieldFill.UPDATE, value = "zw_aid")
    private Long aid;

    @ApiModelProperty("教师备注")
    @TableField("zw_teacher_remark")
    private String teacherRemark;

    @ApiModelProperty("审核员备注")
    @TableField("zw_auditor_remark")
    private String auditorRemark;

    @ApiModelProperty("附件路径")
    @TableField("zw_url")
    private String url;

    @ApiModelProperty("是否展示 0:不展示 1:展示")
    @TableField(fill = FieldFill.INSERT, value = "zw_is_show")
    private Integer isShow;

    @ApiModelProperty("删除角色")
    @TableField(fill = FieldFill.INSERT, value = "zw_delete_roles")
    private String deleteRoles;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

    @ApiModelProperty("test1")
    private String test1;

    @ApiModelProperty("test2")
    private String test2;

    @ApiModelProperty("test3")
    private String test3;

    @ApiModelProperty("test4")
    private String test4;

}
