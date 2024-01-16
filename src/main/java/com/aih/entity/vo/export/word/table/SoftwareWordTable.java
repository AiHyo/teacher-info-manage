package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.SoftwareAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SoftwareWordTable {
    private String softwareName;
    private String stage;
    private Integer status;
    private LocalDate completionDate;
    private Integer teamSize;

    public SoftwareWordTable(SoftwareAudit audit){
        this.softwareName = audit.getSoftwareName();
        this.stage = audit.getStage();
        this.status = audit.getStatus();
        this.completionDate = audit.getCompletionDate();
        this.teamSize = audit.getTeamSize();
    }
}
