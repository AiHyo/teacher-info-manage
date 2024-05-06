package com.aih.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author AiH
 * @since 2023-12-19
 */
@Data
@TableName("request_college_change")
@ApiModel(value = "RequestCollegeChange对象", description = "")
public class RequestCollegeChange implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("转学院申请id")
    @TableId(value = "zw_id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("教师id")
    @TableField("zw_tid")
    private Long tid;

    @ApiModelProperty("原管理员id")
    @TableField("zw_old_aid")
    private Long oldAid;

    @ApiModelProperty("原学院id")
    @TableField("zw_old_cid")
    private Long oldCid;

    @ApiModelProperty("新管理员id")
    @TableField("zw_new_aid")
    private Long newAid;

    @ApiModelProperty("新学院id")
    @TableField("zw_new_cid")
    private Long newCid;

    @ApiModelProperty("原管理员备注")
    @TableField("zw_old_admin_remark")
    private String oldAdminRemark;

    @ApiModelProperty("新管理员备注")
    @TableField("zw_new_admin_remark")
    private String newAdminRemark;

    @ApiModelProperty("申请时间")
    @TableField("zw_create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("审核时间")
    @TableField("zw_audit_time")
    private LocalDateTime auditTime;

    @ApiModelProperty("申请状态 0待审核 1通过 2未通过")
    @TableField("zw_audit_status")
    private Integer auditStatus;

    @ApiModelProperty("删除角色")
    @TableField("zw_delete_roles")
//    @TableField(fill = FieldFill.INSERT)
    private String deleteRoles;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    @TableField("zw_deleted")
    private Integer deleted;

}
