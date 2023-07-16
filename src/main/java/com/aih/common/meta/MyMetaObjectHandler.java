package com.aih.common.meta;

import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("tid", UserInfoContext.getTeacher().getId());
        metaObject.setValue("auditStatus",0); //新增审核数据,默认待审核
        metaObject.setValue("isShow",0);//新增审核数据,自动填充0
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("aid", UserInfoContext.getTeacher().getId());
    }
}
