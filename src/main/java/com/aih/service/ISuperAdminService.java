package com.aih.service;

import com.aih.entity.SuperAdmin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级管理员 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface ISuperAdminService extends IService<SuperAdmin> {



    Map<String, Object> login(SuperAdmin superAdmin);

    SuperAdmin showInfo();

    void logout();

    void updateAdminsStatus(Integer status, List<Long> ids);

    void updateAdminsCid(Long cid, List<Long> ids);

    void resetPassword(List<Long> ids, String password);
}
