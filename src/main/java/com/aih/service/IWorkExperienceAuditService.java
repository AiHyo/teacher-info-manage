package com.aih.service;

import com.aih.entity.WorkExperienceAudit;
import com.aih.entity.vo.audit.WorkExperienceDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工作经历审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IWorkExperienceAuditService extends IService<WorkExperienceAudit> {
    void passWorkExperienceAudit(WorkExperienceAudit academicPaper);

    void rejectWorkExperienceAudit(WorkExperienceAudit academicPaper);

    Page<WorkExperienceDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<WorkExperienceDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    WorkExperienceDto queryDtoById(Long id);
}
