package com.aih.entity.vo.audit;

import com.aih.entity.AcademicPaperAudit;
import lombok.Data;

@Data
public class AcademicPaperDto extends AcademicPaperAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
