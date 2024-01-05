package com.aih.entity.vo.audit;

import com.aih.entity.SoftwareAudit;
import lombok.Data;

@Data
public class SoftwareDto extends SoftwareAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
