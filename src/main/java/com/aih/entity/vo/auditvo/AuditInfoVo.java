package com.aih.entity.vo.auditvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
// === 预览所有专用 === 可整合所有类型的实体类
public class AuditInfoVo {
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
