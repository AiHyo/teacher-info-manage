package com.aih.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.aih.entity.IdentityCardAudit;
import com.aih.entity.Teacher;
import com.aih.entity.vo.AuditInfoDto;
import com.aih.entity.vo.TeacherDetailDto;
import com.aih.entity.vo.TeacherDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deepoove.poi.XWPFTemplate;
import org.apache.ibatis.annotations.Delete;

import java.io.File;
import java.io.IOException;
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

    TeacherDetailDto queryTeacherDtoByTid(Long uid);

    void logout();

    Page<TeacherDto> getTeacherList(Integer pageNum, Integer pageSize, String teacherName, Integer gender, String ethnic, String birthplace, String address);

    IdentityCardAudit queryIdentityCardShowByTid(Long id);

    List<String> getRoleListByTid(Long id);

    XWPFTemplate getWordRender(Teacher teacher) throws IOException;

    ExcelWriter getMyExcelWriter(List<Teacher> teacherList, String fileName, List<String> fieldList);

    File getMyExcelFile(List<Teacher> teacherList, List<String> fieldList);

    File getMyWordFolder(List<Teacher> teacherList) throws IOException;

    File getTeacherAttachmentFolder(List<Teacher> teacherList, List<String> attachmentList);

    Page<AuditInfoDto> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn);
}
