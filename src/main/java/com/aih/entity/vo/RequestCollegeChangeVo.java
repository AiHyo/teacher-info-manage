package com.aih.entity.vo;

import com.aih.entity.RequestCollegeChange;
import lombok.Data;

@Data
public class RequestCollegeChangeVo extends RequestCollegeChange {
    String teacherName;
    String oldAdminName;
    String oldCollegeName;
    String newAdminName;
    String newCollegeName;
}
