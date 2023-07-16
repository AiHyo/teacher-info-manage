package com.aih.service.impl;

import com.aih.entity.AcademicPaperAudit;
import com.aih.entity.AcademicPaperAudit;
import com.aih.mapper.AcademicPaperAuditMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IAcademicPaperAuditService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 论文审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class AcademicPaperAuditServiceImpl extends ServiceImpl<AcademicPaperAuditMapper, AcademicPaperAudit> implements IAcademicPaperAuditService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<AcademicPaperAudit> queryByCid() {
        //先根据cid查询对应学院下的所有tid
        List<Integer> teacherIds = teacherMapper.getAuditorIdsByCid(UserInfoContext.getTeacher().getCid());
        //再根据tid查询所有的论文审核:即学院下的所有审核
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AcademicPaperAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<AcademicPaperAudit> queryByOid() {
        //先根据oid查询对应教研室下的所有tid
        List<Integer> teacherIds = teacherMapper.getTeacherIdsByOid(UserInfoContext.getTeacher().getOid());
        //再根据tid查询所有的论文审核:即教研室下的所有审核
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AcademicPaperAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }
}
