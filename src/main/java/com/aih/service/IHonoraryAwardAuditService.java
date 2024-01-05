package com.aih.service;

import com.aih.entity.HonoraryAwardAudit;
import com.aih.entity.HonoraryAwardAudit;
import com.aih.entity.vo.audit.HonoraryAwardDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 荣誉奖项审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IHonoraryAwardAuditService extends IService<HonoraryAwardAudit> {
    void passHonoraryAwardAudit(HonoraryAwardAudit academicPaper);

    void rejectHonoraryAwardAudit(HonoraryAwardAudit academicPaper);

    Page<HonoraryAwardDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<HonoraryAwardDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    HonoraryAwardDto queryDtoById(Long id);
}
