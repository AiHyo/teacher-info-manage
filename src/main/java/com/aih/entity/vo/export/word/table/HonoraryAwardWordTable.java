package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.HonoraryAwardAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HonoraryAwardWordTable {
    private LocalDate getDate;
    private String type;
    private String content;

    public HonoraryAwardWordTable(HonoraryAwardAudit honoraryAwardAudit){
        this.getDate = honoraryAwardAudit.getGetDate();
        this.type = honoraryAwardAudit.getType() == 1 ? "个人" : "团队";
        this.content = honoraryAwardAudit.getContent();
    }
}
