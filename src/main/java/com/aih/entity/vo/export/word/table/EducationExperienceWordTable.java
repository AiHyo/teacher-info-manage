package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.EducationExperienceAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EducationExperienceWordTable {
    private LocalDate staDate;
    private LocalDate endDate;
    private String school;
    private String major;

    public EducationExperienceWordTable(EducationExperienceAudit educationExperienceAudit){
        this.staDate = educationExperienceAudit.getStaDate();
        this.endDate = educationExperienceAudit.getEndDate();
        this.school = educationExperienceAudit.getSchool();
        this.major = educationExperienceAudit.getMajor();
    }
}
