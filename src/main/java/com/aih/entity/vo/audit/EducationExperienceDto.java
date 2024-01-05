package com.aih.entity.vo.audit;

import com.aih.entity.EducationExperienceAudit;
import lombok.Data;

@Data
public class EducationExperienceDto extends EducationExperienceAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
