package com.aih.service;

import com.aih.entity.Admin;
import com.aih.entity.RequestCollegeChange;
import com.aih.entity.vo.AuditInfoDto;
import com.aih.entity.vo.RequestCollegeChangeDto;
import com.aih.entity.vo.TeacherDto;
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

    Page<TeacherDto> getTeacherList(Integer pageNum, Integer pageSize, String keyword, Long oid, String officeName, String teacherName, Integer isAuditor, Integer gender, String ethnic, String birthplace, String address);

    void resetPassword(List<Long> ids, String password);

    Page<AuditInfoDto> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn);

    Page<RequestCollegeChangeDto> getChangeCollegeRequestList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn);

    Page<RequestCollegeChangeDto> getChangeCollegeAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn);

    void applyChangeTeacherCollege(Long tid, Long newCid, String oldAdminRemark);

    void passChangeCollege(RequestCollegeChange id, String newAdminRemark);

    void refuseChangeCollege(RequestCollegeChange requestCollegeChange, String newAdminRemark);

    String deleteChangeCollege(Long id);

    void deleteTeacher(List<Long> ids);
}
