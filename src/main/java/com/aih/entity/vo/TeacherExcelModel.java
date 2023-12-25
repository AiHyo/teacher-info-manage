package com.aih.entity.vo;

import com.aih.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherExcelModel {
    private Long id;
    private String teacherName;
    private String username;
    private String gender;
    private String idNumber;
//    private String identityCard;  //不在Teacher中
    private String roleList;//不在Teacher中
    private String ethnic;
    private String birthplace;
    private String address;
    private String phone;
    private String collegeName;//不在Teacher中
    private String officeName; //不在Teacher中
    private String educationDegree;
    private String isAuditor;
    private LocalDate startDate;

    //传入Teacher的创建实体类的方法
    public TeacherExcelModel(Teacher teacher){
        this.id = teacher.getId();
        this.teacherName = teacher.getTeacherName();
        this.username = teacher.getUsername();
        this.gender = teacher.getGender() == 1?"男":"女";
        this.idNumber = teacher.getIdNumber();
        this.ethnic = teacher.getEthnic();
        this.birthplace = teacher.getNativePlace();
        this.address = teacher.getAddress();
        this.phone = teacher.getPhone();
        this.educationDegree = teacher.getEducationDegree();
        this.isAuditor = teacher.getIsAuditor()==1?"是":"否";
        this.startDate = teacher.getStartDate();
    }


}
