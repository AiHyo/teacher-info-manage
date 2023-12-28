package com.aih.entity.vo;

import com.aih.entity.*;
import com.aih.entity.vo.audit.AcademicPaperDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetailDto extends TeacherDto {
    //身份证信息
//    private IdentityCardAudit identityCard;
    //论文
    private List<AcademicPaperDto> academicPaperList;
    //教育经历
    private List<EducationExperienceAudit> educationExperienceList;
    //工作经历
    private List<WorkExperienceAudit> workExperienceList;
    //荣誉奖项
    private List<HonoraryAwardAudit> HonoraryAwardList;
    //课题
    private List<TopicAudit> topicList;
    //项目
    private List<ProjectAudit> projectList;
    //软件著作
    private List<SoftwareAudit> softwareList;
}
