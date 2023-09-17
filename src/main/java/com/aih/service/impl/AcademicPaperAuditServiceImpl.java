package com.aih.service.impl;

import com.aih.entity.AcademicPaperAudit;
import com.aih.mapper.*;
import com.aih.service.IAcademicPaperAuditService;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
@Slf4j
public class AcademicPaperAuditServiceImpl extends ServiceImpl<AcademicPaperAuditMapper, AcademicPaperAudit> implements IAcademicPaperAuditService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Resource
    private MyUtil myUtil;

    @Override
    public AcademicPaperAudit queryById(Long id) {
        AcademicPaperAudit findData = this.baseMapper.selectById(id);
        Long tid = findData.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }
        return findData;
    }

    @Override
    public void passAcademicPaperAudit(AcademicPaperAudit academicPaper) {
        Long tid = academicPaper.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        powerIds.remove(tid);//所属教师没有权限
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_AUDIT);
        }
        academicPaper.setAuditStatus(1);//审核通过
        academicPaper.setIsShow(1);//审核通过后自动显示
        this.baseMapper.updateById(academicPaper);
    }

    @Override
    public void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper) {
        Long tid = academicPaper.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        powerIds.remove(tid);//所属教师没有权限
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_AUDIT);
        }
        academicPaper.setAuditStatus(2);//审核驳回
        this.baseMapper.updateById(academicPaper);
    }

    @Override
    public Page<AcademicPaperAudit> queryOwnRecord(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title) {
        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AcademicPaperAudit::getTid, uid) //查自己的
                .eq((auditStatus != null), AcademicPaperAudit::getAuditStatus, auditStatus)
                .like((StringUtils.isNotBlank(title)), AcademicPaperAudit::getTitle, title);//模糊查询
        if (auditStatus != null && auditStatus != 0){
            queryWrapper.and( wrapper -> wrapper
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }
        return this.baseMapper.selectPage(pageInfo,queryWrapper);
    }

    @Override
    public void addDeleteRoles(Long id)
    {
        AcademicPaperAudit academicPaper = this.baseMapper.selectById(id);
        log.info("find academicPaper:{}", academicPaper);
        Long tid = academicPaper.getTid();
        Long uid = UserInfoContext.getUser().getId();

        //特判：未审核的记录,直接正常删除
        if (0 == academicPaper.getAuditStatus()){
            //只能所属教师本人可以操作
            if (tid != uid){
                throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
            }
            this.baseMapper.deleteById(id);
        }

        String deleteRoles = academicPaper.getDeleteRoles();
        //获取有权限操作该条记录的id
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        //没有权力删除:
        if (!powerIds.contains(uid)){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        //已经删除过:
        if (deleteRoles.contains("," + uid + ",")){
            throw new CustomException(CustomExceptionCodeMsg.HAS_DELETE);
        }
        //操作：添加删除角色
        deleteRoles += uid + ",";
        academicPaper.setDeleteRoles(deleteRoles);
        this.baseMapper.updateById(academicPaper);
    }


    @Override
    public Page<AcademicPaperAudit> queryRecordsByOid(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title) {
        //先根据oid查询对应教研室下的所有tid
        List<Long> teacherIds = teacherMapper.getTeacherIdsByOid(UserInfoContext.getUser().getOid());
        //再根据tid查询所有的审核:即教研室下的所有审核
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AcademicPaperAudit::getTid, teacherIds)
                .eq((auditStatus != null), AcademicPaperAudit::getAuditStatus, auditStatus) //0,1,2
                .ge(AcademicPaperAudit::getCreateTime, UserInfoContext.getUser().getCreateDate().atStartOfDay())//上任以来
                .like(StringUtils.isNotBlank(title), AcademicPaperAudit::getTitle, title);//模糊查询
        if (auditStatus != null && auditStatus != 0){
            Long uid = UserInfoContext.getUser().getId();
            queryWrapper.and( wrapper -> wrapper
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }
        return this.baseMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public Page<AcademicPaperAudit> queryRecordsByCid(Page<AcademicPaperAudit> pageInfo, Integer auditStatus, String title) {
        //先根据cid查询对应学院下的所有tid
        List<Long> auditorIds = teacherMapper.getAuditorIdsByCid(UserInfoContext.getUser().getCid());
        //再根据tid查询所有的审核:即学院下的所有审核
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper;
        queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AcademicPaperAudit::getTid, auditorIds)
                .eq((auditStatus != null), AcademicPaperAudit::getAuditStatus, auditStatus) //0,1,2
                .ge(AcademicPaperAudit::getCreateTime, UserInfoContext.getUser().getCreateDate().atStartOfDay())//上任以来
                .like((StringUtils.isNotBlank(title)), AcademicPaperAudit::getTitle, title);//模糊查询
        if (auditStatus != null && auditStatus != 0){
            Long uid = UserInfoContext.getUser().getId();
            queryWrapper.and( wrapper -> wrapper
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }
//        queryWrapper.apply("academic_paper_audit.create_time <= teacher.create_date");
        return this.baseMapper.selectPage(pageInfo, queryWrapper);
    }

        /**
         * 封装一个获取有权限操作该记录的方法
         */
/*        private List<Long> getPowerIds(Long tid){ //id:该条记录的id
            Teacher teacher = teacherMapper.selectById(tid);//获取该记录的所属教师
            Integer isAuditor = teacher.getIsAuditor();
            //获取有权限操作该条记录的id
            List<Long> powerIds = null;
            if (0 == isAuditor) {
                //非审核员(=是教师),则根据教研室id,查审核员id
                Long oid = teacher.getOid();
                powerIds = teacherMapper.getAuditorIdsByOid(oid);
                log.info("该记录有权限的审核员id:{}", powerIds);
            }else {
                //是审核员,则根据学院id,查管理员id
                Long cid = teacher.getCid();
                powerIds = adminMapper.getAdminIdsByCid(cid);
                log.info("该记录有权限的管理员id:{}", powerIds);
            }
            powerIds.add(tid);//再加上所属教师的id
            return powerIds;
        }*/

}
