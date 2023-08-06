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

    List<AcademicPaperAudit> queryAllByCid();
    List<AcademicPaperAudit> queryNotAuditByCid();

    List<AcademicPaperAudit> queryAllByOid();

    List<AcademicPaperAudit> queryNotAuditByOid();

    List<AcademicPaperAudit> queryPassAuditByCid();

    List<AcademicPaperAudit> queryRejectAuditByCid();

    List<AcademicPaperAudit> queryPassAuditByOid();

    List<AcademicPaperAudit> queryRejectAuditByOid();

    List<AcademicPaperAudit> queryOwnAll();

    List<AcademicPaperAudit> queryOwnNotAudit();

    List<AcademicPaperAudit> queryOwnPassAudit();

    List<AcademicPaperAudit> queryOwnRejectAudit();

    void addDeleteRoles(Long id);


    AcademicPaperAudit queryById(Long id);

    void passAcademicPaperAudit(AcademicPaperAudit academicPaper);

    void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper);
}
