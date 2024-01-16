package com.aih.entity.vo.export.word;

import com.aih.entity.vo.TeacherDetailVo;
import com.aih.entity.vo.export.word.table.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherWordModel{
    private Long id;
    private String teacherName;
    private String username;
    private String gender;
    private String idNumber;
    private String roleList;//不在Teacher中
    private String ethnic;
    private String politicsStatus;
    private String nativePlace;
    private String address;
    private String phone;
    private String collegeName;//不在Teacher中
    private String officeName; //不在Teacher中
    private String educationDegree;
    private String isAuditor;
    private LocalDate startDate;

    private List<AcademicPaperWordTable> academicPaperList;
    private List<EducationExperienceWordTable> educationExperienceList;
    private List<WorkExperienceWordTable> workExperienceList;
    private List<HonoraryAwardWordTable> honoraryAwardList;
    private List<TopicWordTable> topicList;
    private List<ProjectWordTable> projectList;
    private List<SoftwareWordTable> softwareList;

    public TeacherWordModel(TeacherDetailVo dto){
        this.id = dto.getId();
        this.teacherName = dto.getTeacherName();
        this.username = dto.getUsername();
        if (dto.getGender() != null) {
            this.gender = dto.getGender() == 1?"男":"女";
        }
        this.idNumber = dto.getIdNumber();
        this.ethnic = dto.getEthnic();
        this.politicsStatus = dto.getPoliticsStatus();
        this.nativePlace = dto.getNativePlace();
        this.address = dto.getAddress();
        this.phone = dto.getPhone();
        this.educationDegree = dto.getEducationDegree();
        if (dto.getIsAuditor() != null) {
            this.isAuditor = dto.getIsAuditor() == 1?"是":"否";
        }
        this.startDate = dto.getStartDate();
        this.collegeName = dto.getCollegeName();
        this.officeName = dto.getOfficeName();
        this.roleList = dto.getRoleList();
        this.academicPaperList = dto.getAcademicPaperList().stream().map(AcademicPaperWordTable::new).collect(Collectors.toList());
        this.educationExperienceList = dto.getEducationExperienceList().stream().map(EducationExperienceWordTable::new).collect(Collectors.toList());
        this.workExperienceList = dto.getWorkExperienceList().stream().map(WorkExperienceWordTable::new).collect(Collectors.toList());
        this.honoraryAwardList = dto.getHonoraryAwardList().stream().map(HonoraryAwardWordTable::new).collect(Collectors.toList());
        this.topicList = dto.getTopicList().stream().map(TopicWordTable::new).collect(Collectors.toList());
        this.projectList = dto.getProjectList().stream().map(ProjectWordTable::new).collect(Collectors.toList());
        this.softwareList = dto.getSoftwareList().stream().map(SoftwareWordTable::new).collect(Collectors.toList());
    }
}
