package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.WorkExperienceAudit;
import lombok.Data;

@Data
public class WorkExperienceVo extends WorkExperienceAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
