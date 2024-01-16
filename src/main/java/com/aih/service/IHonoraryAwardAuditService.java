package com.aih.service;

import com.aih.entity.audit.HonoraryAwardAudit;
import com.aih.entity.vo.auditvo.HonoraryAwardVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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

    Page<HonoraryAwardVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<HonoraryAwardVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    HonoraryAwardVo queryDtoById(Long id);

    //Dto转换
    HonoraryAwardVo getDto(HonoraryAwardAudit honoraryAward);
}
