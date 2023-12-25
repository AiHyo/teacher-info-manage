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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("教师id")
    private Long tid;

    @ApiModelProperty("原管理员id")
    private Long oldAid;

    @ApiModelProperty("原学院id")
    private Long oldCid;

    @ApiModelProperty("新管理员id")
    private Long newAid;

    @ApiModelProperty("新学院id")
    private Long newCid;

    @ApiModelProperty("原管理员备注")
    private String oldAdminRemark;

    @ApiModelProperty("新管理员备注")
    private String newAdminRemark;

    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty("审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty("申请状态 0待审核 1通过 2未通过")
    private Integer auditStatus;

    @ApiModelProperty("删除角色")
//    @TableField(fill = FieldFill.INSERT)
    private String deleteRoles;

    @ApiModelProperty("逻辑删除")
    @TableLogic
    private Integer deleted;

}
