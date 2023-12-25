package com.aih.common.meta;

import com.aih.entity.Admin;
import com.aih.entity.Teacher;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Class<?> cla = metaObject.getOriginalObject().getClass();
        if (cla == Admin.class){
            metaObject.setValue("createDate", LocalDate.now());
            metaObject.setValue("status",1); //默认启用
            return ;
        } else if (cla == Teacher.class) {
            metaObject.setValue("createDate", LocalDate.now());
            return ;
        }
        //===========================审核材料信息类===================================
//        metaObject.setValue("createDate", LocalDate.now());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("tid", UserInfoContext.getUser().getId());
        metaObject.setValue("auditStatus",0); //新增审核数据,默认待审核
        metaObject.setValue("isShow",0);      //待审核数据,默认非有效显示
        metaObject.setValue("deleteRoles",",");//默认删除角色为空
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("auditTime", LocalDateTime.now());
        metaObject.setValue("aid", UserInfoContext.getUser().getId());
    }
}
