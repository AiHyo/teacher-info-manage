package com.aih.service;

import com.aih.entity.audit.AcademicPaperAudit;
import com.aih.entity.vo.AdminVo;
import com.aih.entity.vo.auditvo.AcademicPaperVo;
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
    void passAcademicPaperAudit(AcademicPaperAudit academicPaper);

    void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper);

    Page<AcademicPaperVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<AcademicPaperVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    AcademicPaperVo queryDtoById(Long id);

    AcademicPaperVo getDto(AcademicPaperAudit academicPaperAudit);
}
