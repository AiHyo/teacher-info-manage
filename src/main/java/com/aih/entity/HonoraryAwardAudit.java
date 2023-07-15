package com.aih.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 荣誉奖项审核
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("honorary_award_audit")
@ApiModel(value = "HonoraryAwardAudit对象", description = "荣誉奖项审核")
public class HonoraryAwardAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("荣誉奖项id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属教师id")
    private Long tid;

    @ApiModelProperty("获奖时间")
    private LocalDate getDate;

    @ApiModelProperty("类型 0:团队 1:个人")
    private Integer type;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("附件路径 ")
    private String url;

    @ApiModelProperty("审核状态 0:待审核 1:通过 2:驳回")
    private Integer auditStatus;

    @ApiModelProperty("提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("教师备注")
    private String teacherRemark;

    @ApiModelProperty("审核员备注")
    private String auditorRemark;

    @ApiModelProperty("是否展示 0:不展示 1:展示")
    private Integer isShow;

    @ApiModelProperty("逻辑删除 0:未删除 1:已删除")
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
