package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.WorkExperienceAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WorkExperienceWordTable {
    private LocalDate staDate;
    private LocalDate endDate;
    private String companyName;
    private String position;

    public WorkExperienceWordTable(WorkExperienceAudit audit){
        this.staDate = audit.getStaDate();
        this.endDate = audit.getEndDate();
        this.companyName = audit.getCompanyName();
        this.position = audit.getPosition();
    }
}
