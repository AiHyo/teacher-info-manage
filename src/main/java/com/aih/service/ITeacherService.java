package com.aih.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.aih.entity.Teacher;
import com.aih.entity.vo.auditvo.AuditInfoVo;
import com.aih.entity.vo.TeacherDetailVo;
import com.aih.entity.vo.TeacherVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deepoove.poi.XWPFTemplate;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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

    TeacherDetailVo queryTeacherDetailDtoByTid(Long uid);

    void logout();

    Page<TeacherVo> getTeacherList(Integer pageNum, Integer pageSize, String teacherName, Integer gender, String ethnic, String birthplace, String address);

    List<String> getRoleListByTid(Long id);

    XWPFTemplate getWordRenderByTid(Long teacher) throws IOException;

    ExcelWriter getMyExcelWriter(List<Teacher> teacherList, String fileName, List<String> fieldList);

    File getMyExcelFile(List<Teacher> teacherList, List<String> fieldList);

    File getMyWordFolder(List<Teacher> teacherList) throws IOException;

    File getTeacherAttachmentFolder(List<Teacher> teacherList, List<String> attachmentList);

    Page<AuditInfoVo> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn);

    List<AuditInfoVo> getAllAuditInfoDtoList(List<Long> auditorCanAuditTids, Integer auditStatus, boolean onlyOwn, Long uid, LocalDate createDate);

    List<Map<String, Object>> getTeacherCountByCollege();

    boolean checkUsernameExist(String username);
}
