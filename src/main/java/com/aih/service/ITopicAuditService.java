package com.aih.service;

import com.aih.entity.TopicAudit;
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

    List<TopicAudit> queryByCid();

    List<TopicAudit> queryByOid();
}
