package com.aih.service;

import com.aih.entity.WorkExperienceAudit;
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

    List<WorkExperienceAudit> queryByCid();

    List<WorkExperienceAudit> queryByOid();
}
