package com.aih.service;

import com.aih.entity.AcademicPaperAudit;
import com.aih.entity.vo.audit.AcademicPaperDto;
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

    Page<AcademicPaperDto> queryRecordsByCid(Integer pageNum, Integer pageSize, Integer auditStatus, String title, Boolean onlyOwn);

    Page<AcademicPaperDto> queryRecordsByOid(Integer pageNum, Integer pageSize, Integer auditStatus, String title, Boolean onlyOwn);


    String deleteRecord(Long id);


    AcademicPaperDto queryDtoById(Long id);

    void passAcademicPaperAudit(AcademicPaperAudit academicPaper);

    void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper);

    Page<AcademicPaperDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String title);

    void deleteOwnInfo(Long id);

    Page<AcademicPaperDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn);
}
