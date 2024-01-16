package com.aih.service.impl;

import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Role;
import com.aih.entity.TeacherRole;
import com.aih.mapper.RoleMapper;
import com.aih.mapper.TeacherRoleMapper;
import com.aih.service.IRoleService;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private TeacherRoleMapper teacherRoleMapper;
    @Override
    public void updateTeacherRole(Long tid, List<Long> rids) {
        // == 开始修改 ==
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId,rids);
        List<Role> roleList = this.baseMapper.selectList(queryWrapper);
        if (roleList.size()!=rids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        //删除tid的所有数据
        teacherRoleMapper.deleteTeacherRoleByTid(tid);
        //插入新的数据
        for (Long rid : rids) {
            teacherRoleMapper.insert(new TeacherRole(tid,rid));
        }
    }
}
