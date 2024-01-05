package com.aih.service;

import com.aih.entity.ProjectAudit;
import com.aih.entity.TopicAudit;
import com.aih.entity.vo.audit.TopicDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    Page<TopicDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword);

    Page<TopicDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword);

    void deleteOwnInfo(Long id);

    String deleteRecord(Long id);

    TopicDto queryDtoById(Long id);
}
