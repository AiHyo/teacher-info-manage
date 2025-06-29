package com.aih.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private RoleType roleType;
    private Long id;
    private Long oid;
    private Long cid;
    private LocalDate createDate;
//    private String EntityType;
}
