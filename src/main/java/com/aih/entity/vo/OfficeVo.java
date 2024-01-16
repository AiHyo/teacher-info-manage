package com.aih.entity.vo;

import com.aih.entity.Office;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeVo extends Office {
    String CollegeName;
}
