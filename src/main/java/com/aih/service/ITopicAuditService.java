package com.aih.service;

import com.aih.entity.audit.TopicAudit;
import com.aih.entity.vo.auditvo.TopicVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课题审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface ITopicAuditService extends IService<TopicAudit> {
    void passTopicAudit(TopicAudit academicPaper);

    void rejectTopicAudit(TopicAudit academicPaper);

    Page<TopicVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<TopicVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    TopicVo queryDtoById(Long id);

    TopicVo getDto(TopicAudit audit);
}
