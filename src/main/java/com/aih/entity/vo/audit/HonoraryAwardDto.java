package com.aih.entity.vo.audit;

import com.aih.entity.HonoraryAwardAudit;
import lombok.Data;

@Data
public class HonoraryAwardDto extends HonoraryAwardAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
