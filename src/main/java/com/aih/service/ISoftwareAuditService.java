package com.aih.service;

import com.aih.entity.SoftwareAudit;
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

    List<SoftwareAudit> queryByCid();

    List<SoftwareAudit> queryByOid();
}
