package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.AcademicPaperAudit;
import lombok.Data;

@Data
public class AcademicPaperVo extends AcademicPaperAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
