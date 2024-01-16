package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.SoftwareAudit;
import lombok.Data;

@Data
public class SoftwareVo extends SoftwareAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
