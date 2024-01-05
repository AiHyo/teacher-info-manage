package com.aih.service;

import com.aih.entity.SoftwareAudit;
import com.aih.entity.SoftwareAudit;
import com.aih.entity.vo.audit.SoftwareDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    Page<SoftwareDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<SoftwareDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    SoftwareDto queryDtoById(Long id);
}
