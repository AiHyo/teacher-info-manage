package com.aih.entity.vo.audit;

import com.aih.entity.TopicAudit;
import lombok.Data;

@Data
public class TopicDto extends TopicAudit {
    private String teacherName;
    private String officeName;
    private String collegeName;
    private String auditorName;
}
