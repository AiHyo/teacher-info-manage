package com.aih.service;

import com.aih.entity.EducationExperienceAudit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 教育经历审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IEducationExperienceAuditService extends IService<EducationExperienceAudit> {

    List<EducationExperienceAudit> queryByOid();
    List<EducationExperienceAudit> queryByCidAndAuditStatus();

    List<EducationExperienceAudit> queryByCid();

}
