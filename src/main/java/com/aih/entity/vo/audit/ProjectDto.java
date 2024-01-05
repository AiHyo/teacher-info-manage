package com.aih.entity.vo.audit;

import com.aih.entity.ProjectAudit;
import lombok.Data;

@Data
public class ProjectDto extends ProjectAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
