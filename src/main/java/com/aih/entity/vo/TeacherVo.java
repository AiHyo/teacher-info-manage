package com.aih.entity.vo;

import com.aih.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherVo extends Teacher {
    //所在<学院>名称
    private String collegeName;
    //所在<教研室>名称
    private String officeName;
    //职务
    private String roleList;
}
