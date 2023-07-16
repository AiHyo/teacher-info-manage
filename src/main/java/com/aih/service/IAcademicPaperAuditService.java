package com.aih.service;

import com.aih.entity.AcademicPaperAudit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 论文审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IAcademicPaperAuditService extends IService<AcademicPaperAudit> {

    List<AcademicPaperAudit> queryByCid();

    List<AcademicPaperAudit> queryByOid();
}
