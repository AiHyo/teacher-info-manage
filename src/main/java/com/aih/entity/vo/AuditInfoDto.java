package com.aih.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuditInfoDto {
    String auditType; //审核类型
    Long id; //审核对象id
    String auditStatus; //审核状态
    LocalDateTime createTime; //创建时间
    LocalDateTime auditTime;  //审核时间
    String teacherName; //教师姓名
    String auditName; //审核人姓名
    String officeName; //教研室名称
    String collegeName; //学院名称
}
