package com.aih.entity.vo.export.word.table;

import com.aih.entity.audit.TopicAudit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TopicWordTable {
    private String topicName;
    private String topicType;
    private LocalDate staDate;
    private LocalDate endDate;
    private Integer teamSize;

    public TopicWordTable(TopicAudit audit){
        this.topicName = audit.getTopicName();
        this.topicType = audit.getTopicType();
        this.staDate = audit.getStaDate();
        this.endDate = audit.getEndDate();
        this.teamSize = audit.getTeamSize();
    }
}
