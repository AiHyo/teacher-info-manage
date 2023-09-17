package com.aih.service;

import com.aih.entity.IdentityCardAudit;
import com.aih.entity.Teacher;
import com.aih.entity.vo.TeacherDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教师(用户) 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface ITeacherService extends IService<Teacher> {

    Map<String,Object> login(Teacher teacher);

    TeacherDto queryTeacherDtoByTid(Long uid);

    void logout();

    Page<Teacher> getTeacherList(Page<Teacher> pageInfo, Integer pageNum, Integer pageSize, String teacherName, Integer gender, String ethnic, String birthplace, String address);

    IdentityCardAudit queryIdentityCardShowByTid(Long id);

    List<String> getRoleListByTid(Long id);

}
