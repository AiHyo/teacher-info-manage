package com.aih.entity.vo;

import com.aih.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVo extends Admin {
    String collegeName;

    public AdminVo(Admin admin,String collegeName) {
        BeanUtils.copyProperties(admin,this);
        this.collegeName = collegeName;
    }
}
