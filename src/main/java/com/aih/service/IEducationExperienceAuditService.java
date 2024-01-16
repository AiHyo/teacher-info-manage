package com.aih.service;

import com.aih.entity.audit.EducationExperienceAudit;
import com.aih.entity.vo.auditvo.EducationExperienceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 教育经历审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IEducationExperienceAuditService extends IService<EducationExperienceAudit> {

    void passEducationExperienceAudit(EducationExperienceAudit academicPaper);

    void rejectEducationExperienceAudit(EducationExperienceAudit academicPaper);

    Page<EducationExperienceVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<EducationExperienceVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    EducationExperienceVo queryDtoById(Long id);

    EducationExperienceVo getDto(EducationExperienceAudit educationExperienceAudit);
}
