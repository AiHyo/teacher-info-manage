package com.aih.service.impl;

import com.aih.entity.TopicAudit;
import com.aih.mapper.TeacherMapper;
import com.aih.mapper.TopicAuditMapper;
import com.aih.service.ITopicAuditService;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课题审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
public class TopicAuditServiceImpl extends ServiceImpl<TopicAuditMapper, TopicAudit> implements ITopicAuditService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<TopicAudit> queryByCid() {
        //先根据cid查询对应学院下的所有审核员tid
        List<Long> teacherIds = teacherMapper.getCanAuditTidsByCid(UserInfoContext.getUser().getCid());
        //再根据tid查询所有的课题审核:即学院下的所有审核
        LambdaQueryWrapper<TopicAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TopicAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<TopicAudit> queryByOid() {
        //先根据oid查询对应教研室下的所有tid
        List<Long> teacherIds = teacherMapper.getTeacherIdsByOid(UserInfoContext.getUser().getOid());
        //再根据tid查询所有的课题审核:即教研室下的所有审核
        LambdaQueryWrapper<TopicAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TopicAudit::getTid, teacherIds);
        return this.baseMapper.selectList(queryWrapper);
    }
}
