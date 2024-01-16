package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.TopicAudit;
import lombok.Data;

@Data
public class TopicVo extends TopicAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
