package com.aih.entity.vo.auditvo;

import com.aih.entity.audit.HonoraryAwardAudit;
import lombok.Data;

@Data
public class HonoraryAwardVo extends HonoraryAwardAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
