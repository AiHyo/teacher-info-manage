package com.aih.service;

import com.aih.entity.AcademicPaperAudit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 论文审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IAcademicPaperAuditService extends IService<AcademicPaperAudit> {

    Page<AcademicPaperAudit> queryAllByCid(Page<AcademicPaperAudit> pageInfo, String title);
    Page<AcademicPaperAudit> queryNotAuditByCid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryAllByOid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryNotAuditByOid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryPassAuditByCid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryRejectAuditByCid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryPassAuditByOid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryRejectAuditByOid(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryOwnAll(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryOwnNotAudit(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryOwnPassAudit(Page<AcademicPaperAudit> pageInfo, String title);

    Page<AcademicPaperAudit> queryOwnRejectAudit(Page<AcademicPaperAudit> pageInfo, String title);

    void addDeleteRoles(Long id);


    AcademicPaperAudit queryById(Long id);

    void passAcademicPaperAudit(AcademicPaperAudit academicPaper);

    void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper);
}
