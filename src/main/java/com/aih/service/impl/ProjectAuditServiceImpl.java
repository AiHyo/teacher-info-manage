package com.aih.service.impl;

import com.aih.entity.ProjectAudit;
import com.aih.mapper.ProjectAuditMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IProjectAuditService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class ProjectAuditServiceImpl extends ServiceImpl<ProjectAuditMapper, ProjectAudit> implements IProjectAuditService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<ProjectAudit> queryByCid() {
        //先根据cid查询对应学院下的所有审核员tid
        List<Long> teacherIds = teacherMapper.getAuditorIdsByCid(UserInfoContext.getUser().getCid());
        //再根据tid查询所有的项目审核:即学院下的所有审核
        LambdaQueryWrapper<ProjectAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProjectAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ProjectAudit> queryByOid() {
        //先根据oid查询对应教研室下的所有tid
        List<Long> teacherIds = teacherMapper.getTeacherIdsByOid(UserInfoContext.getUser().getOid());
        //再根据tid查询所有的项目审核:即教研室下的所有审核
        LambdaQueryWrapper<ProjectAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProjectAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }
}
