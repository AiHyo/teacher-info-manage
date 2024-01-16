package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.ProjectAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProjectWordTable {
    private String projectName;
    private String projectType;
    private LocalDate completionDate;
    private Integer teamSize;

    public ProjectWordTable(ProjectAudit projectAudit){
        this.projectName = projectAudit.getProjectName();
        this.projectType = projectAudit.getProjectType();
        this.completionDate = projectAudit.getCompletionDate();
        this.teamSize = projectAudit.getTeamSize();
    }
}
