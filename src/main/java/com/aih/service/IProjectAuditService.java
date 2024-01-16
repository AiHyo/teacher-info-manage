package com.aih.service;

import com.aih.entity.audit.ProjectAudit;
import com.aih.entity.vo.auditvo.ProjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IProjectAuditService extends IService<ProjectAudit> {
    void passProjectAudit(ProjectAudit academicPaper);

    void rejectProjectAudit(ProjectAudit academicPaper);

    Page<ProjectVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<ProjectVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    ProjectVo queryDtoById(Long id);

    ProjectVo getDto(ProjectAudit projectAudit);
}
