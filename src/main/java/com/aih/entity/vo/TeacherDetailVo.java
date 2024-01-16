package com.aih.entity.vo;

import com.aih.entity.vo.auditvo.*;
import com.aih.entity.vo.export.word.table.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetailVo extends TeacherVo {
    //论文
    private List<AcademicPaperVo> academicPaperList;
    //教育经历
    private List<EducationExperienceVo> educationExperienceList;
    //工作经历
    private List<WorkExperienceVo> workExperienceList;
    //荣誉奖项
    private List<HonoraryAwardVo> honoraryAwardList;
    //课题
    private List<TopicVo> topicList;
    //项目
    private List<ProjectVo> projectList;
    //软件著作
    private List<SoftwareVo> softwareList;
}
