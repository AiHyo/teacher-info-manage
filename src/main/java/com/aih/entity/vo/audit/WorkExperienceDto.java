package com.aih.entity.vo.audit;

import com.aih.entity.WorkExperienceAudit;
import lombok.Data;

@Data
public class WorkExperienceDto extends WorkExperienceAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
