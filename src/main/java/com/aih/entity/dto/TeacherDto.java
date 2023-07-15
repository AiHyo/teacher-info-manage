package com.aih.entity.dto;

import com.aih.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto extends Teacher {

    //所在<学院>名称
    private String collegeName;
    //所在<教研室>名称
    private String officeName;
    //身份证信息
    private IdentityCardAudit identityCard;
    //职务
    private List<String> roleList;
    //教育经历
    private List<EducationExperienceAudit> educationExperienceList;
    //工作经历
    private List<WorkExperienceAudit> workExperienceList;
    //荣誉奖项
    private List<HonoraryAwardAudit> HonoraryAwardList;
    //课题
    private List<TopicAudit> topicList;
    //论文
    private List<AcademicPaperAudit> academicPaperList;
    //项目
    private List<ProjectAudit> projectList;
    //软件著作
    private List<SoftwareAudit> softwareList;
}
