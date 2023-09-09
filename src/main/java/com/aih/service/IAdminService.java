package com.aih.service;

import com.aih.entity.Admin;
import com.aih.entity.Teacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IAdminService extends IService<Admin> {

    Map<String, Object> login(Admin admin);

    Admin showInfo();

    void updateIsAuditor(Integer isAuditor, List<Long> ids);

    void logout();

    Page<Teacher> getTeacherList(Page<Teacher> pageInfo, String keyword, Integer isAuditor, Integer gender, String ethnic, String birthplace, String address);

}
