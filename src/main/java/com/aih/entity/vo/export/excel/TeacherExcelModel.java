package com.aih.entity.vo.export.excel;

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
    private String deskId;
    private String teacherName;
    private String username;
    private String gender;
    private String identityCard;
//    private String identityCard;  //不在Teacher中
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

    //传入Teacher的创建实体类的方法
    public TeacherExcelModel(Teacher teacher){
        this.id = teacher.getId();
        Long deskId1 = teacher.getDeskId();
        this.deskId = deskId1==null?"":deskId1.toString();
        this.teacherName = teacher.getTeacherName();
        this.username = teacher.getUsername();
        if (teacher.getGender()!=null){
            this.gender = teacher.getGender() == 1?"男":"女";
        }
        this.identityCard = teacher.getIdNumber();
        this.ethnic = teacher.getEthnic();
        this.politicsStatus = teacher.getPoliticsStatus();
        this.nativePlace = teacher.getNativePlace();
        this.address = teacher.getAddress();
        this.phone = teacher.getPhone();
        this.educationDegree = teacher.getEducationDegree();
        if (teacher.getIsAuditor()!=null){
            this.isAuditor = teacher.getIsAuditor()==1?"是":"否";
        }
        this.startDate = teacher.getStartDate();
    }


}
