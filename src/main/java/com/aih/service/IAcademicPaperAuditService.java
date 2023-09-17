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

    Page<AcademicPaperAudit> queryRecordsByCid(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title);

    Page<AcademicPaperAudit> queryRecordsByOid(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title);


    void addDeleteRoles(Long id);


    AcademicPaperAudit queryById(Long id);

    void passAcademicPaperAudit(AcademicPaperAudit academicPaper);

    void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper);

    Page<AcademicPaperAudit> queryOwnRecord(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title);
}
