package com.aih.service;

import com.aih.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
public interface IRoleService extends IService<Role> {

    void updateTeacherRole(Long tid, List<Long> rids);
}
