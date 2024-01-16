package com.aih.service;

import com.aih.entity.audit.SoftwareAudit;
import com.aih.entity.vo.auditvo.SoftwareVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 软件著作审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface ISoftwareAuditService extends IService<SoftwareAudit> {
    void passSoftwareAudit(SoftwareAudit academicPaper);

    void rejectSoftwareAudit(SoftwareAudit academicPaper);

    Page<SoftwareVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<SoftwareVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    SoftwareVo queryDtoById(Long id);

    SoftwareVo getDto(SoftwareAudit softwareAudit);
}
