package com.aih.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeWithOfficeVo {
    private Long id;
    private String collegeName;
    private List<OfficeInCollege> officeList;

}
