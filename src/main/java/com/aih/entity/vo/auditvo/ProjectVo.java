package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.ProjectAudit;
import lombok.Data;

@Data
public class ProjectVo extends ProjectAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
