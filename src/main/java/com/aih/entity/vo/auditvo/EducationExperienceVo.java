package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.EducationExperienceAudit;
import lombok.Data;

@Data
public class EducationExperienceVo extends EducationExperienceAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
