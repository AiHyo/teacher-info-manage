package com.aih.service.impl;

import com.aih.entity.EducationExperienceAudit;
import com.aih.mapper.EducationExperienceAuditMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IEducationExperienceAuditService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 教育经历审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class EducationExperienceAuditServiceImpl extends ServiceImpl<EducationExperienceAuditMapper, EducationExperienceAudit> implements IEducationExperienceAuditService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<EducationExperienceAudit> queryByCid() {
        //先根据cid查询对应学院下的所有审核员tid
        List<Long> teacherIds = teacherMapper.getAuditorIdsByCid(UserInfoContext.getUser().getCid());
        //再根据tid查询所有的教育经历审核:即学院下的所有审核
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EducationExperienceAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }
    @Override
    public List<EducationExperienceAudit> queryByCidAndAuditStatus() {
        List<Long> teacherIds = teacherMapper.getAuditorIdsByCid(UserInfoContext.getUser().getCid());
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EducationExperienceAudit::getTid, teacherIds);
        queryWrapper.eq(EducationExperienceAudit::getAuditStatus,0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<EducationExperienceAudit> queryByOid() {
        //先根据oid查询对应教研室下的所有tid
        List<Long> teacherIds = teacherMapper.getTeacherIdsByOid(UserInfoContext.getUser().getOid());
        //再根据tid查询所有的教育经历审核:即教研室下的所有审核
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EducationExperienceAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }


}
