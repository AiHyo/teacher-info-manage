package com.aih.service;

import com.aih.entity.audit.IdentityCardAudit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 身份证审核 服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
public interface IIdentityCardAuditService extends IService<IdentityCardAudit> {

    List<IdentityCardAudit> queryByOid();

    List<IdentityCardAudit> queryByCid();
}
