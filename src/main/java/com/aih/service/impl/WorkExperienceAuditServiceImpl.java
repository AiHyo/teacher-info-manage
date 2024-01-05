package com.aih.service.impl;

import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.*;
import com.aih.entity.vo.audit.WorkExperienceDto;
import com.aih.mapper.*;
import com.aih.service.IWorkExperienceAuditService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.RoleType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作经历审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class WorkExperienceAuditServiceImpl extends ServiceImpl<WorkExperienceAuditMapper, WorkExperienceAudit> implements IWorkExperienceAuditService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Resource
    private MyUtil myUtil;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CollegeMapper collegeMapper;


    @Override
    public WorkExperienceDto queryDtoById(Long id) {
        WorkExperienceAudit findData = this.baseMapper.selectById(id);
        if (findData == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        Long tid = findData.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }
        WorkExperienceDto dto = this.getDto(findData);
        return dto;
    }

    @Override
    public void passWorkExperienceAudit(WorkExperienceAudit project) {
        project.setAuditStatus(1);//审核通过
        project.setIsShow(1);//审核通过后自动显示
        this.baseMapper.updateById(project);
    }

    @Override
    public void rejectWorkExperienceAudit(WorkExperienceAudit project) {
        project.setAuditStatus(2);//审核驳回
        this.baseMapper.updateById(project);
    }

    @Override
    public Page<WorkExperienceDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword) {
        Page<WorkExperienceAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<WorkExperienceDto> dtoPageInfo = new Page<>(pageNum, pageSize);

        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkExperienceAudit::getTid, uid) //查自己的
                .eq((auditStatus != null), WorkExperienceAudit::getAuditStatus, auditStatus)
                .like((StringUtils.isNotBlank(keyword)), WorkExperienceAudit::getCompanyName, keyword)//模糊查询
                .orderByAsc(auditStatus==null, WorkExperienceAudit::getAuditStatus)//先按审核状态升序 未审核=>通过=>未通过
                .orderByDesc(WorkExperienceAudit::getCreateTime);//再按创建时间倒序
        if (auditStatus != null && auditStatus != 0){
            queryWrapper.and( wrapper -> wrapper
                    .notLike(WorkExperienceAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(WorkExperienceAudit::getDeleteRoles));
        }
        this.baseMapper.selectPage(pageInfo,queryWrapper);
        //遍历每一条records(当前页下的所有数据)
        List<WorkExperienceDto> collect = pageInfo.getRecords().stream().map((item) -> {
            WorkExperienceDto dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }

    @Override
    public Page<WorkExperienceDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword) {
        Page<WorkExperienceAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<WorkExperienceDto> dtoPageInfo = new Page<>(pageNum, pageSize);
        //getCanAuditTidsByOid 根据oid查询有权利审核的
        List<Long> queryTids = null;
        if (UserInfoContext.getUser().getRoleType() == RoleType.AUDITOR){
            queryTids = teacherMapper.getCanAuditTidsByOid(UserInfoContext.getUser().getOid());
        }else if (UserInfoContext.getUser().getRoleType() == RoleType.ADMIN){
            queryTids = teacherMapper.getCanAuditTidsByCid(UserInfoContext.getUser().getCid());
        }
        log.info("queryTids:{}", queryTids);
        if (queryTids != null && queryTids.isEmpty()) { //没有找到教师就返回空的
            return dtoPageInfo;
        }
        //再根据tid查询所有的审核:即教研室下的所有审核
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(WorkExperienceAudit::getTid, queryTids)
                .eq((auditStatus != null), WorkExperienceAudit::getAuditStatus, auditStatus) //0,1,2
                .ge(WorkExperienceAudit::getCreateTime, UserInfoContext.getUser().getCreateDate().atStartOfDay())//上任以来(针对未审核的)
                .eq(onlyOwn, WorkExperienceAudit::getAid, UserInfoContext.getUser().getId())//只看自己的(针对已审核的)
                .like((StringUtils.isNotBlank(keyword)), WorkExperienceAudit::getCompanyName, keyword)//模糊查询
                .orderByAsc(auditStatus==null, WorkExperienceAudit::getAuditStatus)
                .orderByDesc(WorkExperienceAudit::getCreateTime);//审核状态相同的则按创建时间越晚的显示在最前
        // 删除过记录的情况：需要判断删除角色
        if (auditStatus != null && auditStatus != 0){
            Long uid = UserInfoContext.getUser().getId();
            queryWrapper.and( wrapper -> wrapper //选出没有在删除角色中的 如果是未审核的,不允许有删除角色
                    .notLike(WorkExperienceAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(WorkExperienceAudit::getDeleteRoles));
        }
//        queryWrapper.apply("academic_paper_audit.create_time <= teacher.create_date");
        this.baseMapper.selectPage(pageInfo, queryWrapper); //
        //遍历每一条records(当前页下的所有数据)
        List<WorkExperienceDto> collect = pageInfo.getRecords().stream().map((item) -> {
            WorkExperienceDto dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());

        //对象拷贝，拷贝一下查询到的条目数
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }


    @Override
    public void deleteOwnInfo(Long id) {
        WorkExperienceAudit project = this.checkIdExistAndReturn(id);
        Long uid = UserInfoContext.getUser().getId();
        Long tid = project.getTid();
        if (!tid.equals(uid)){//只能所属教师本人可以操作
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (1 != project.getAuditStatus()){//只能删除审核通过的
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (0 == project.getIsShow()){//只能删除未删除的
            throw new CustomException(CustomExceptionCodeMsg.HAS_DELETE);
        }
        LambdaUpdateWrapper<WorkExperienceAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WorkExperienceAudit::getId, id)
                .set(WorkExperienceAudit::getIsShow, 0);
        this.baseMapper.update(null, updateWrapper);//只更新isShow字段,不受自动填充影响
    }


    @Override
    public String deleteRecord(Long id)
    {
        WorkExperienceAudit project = this.checkIdExistAndReturn(id);
        log.info("find project:{}", project);
        Long tid = project.getTid();
        Long uid = UserInfoContext.getUser().getId();
        //特判：未审核的记录,直接正常删除
        if (0 == project.getAuditStatus()){
            //只能所属教师本人可以操作
            log.info("auditStatus=0,tid:{},uid:{}", tid, uid);
            if (!Objects.equals(tid, uid)){
                throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
            }
            this.baseMapper.deleteById(id);
            return "删除审核申请成功";
        }
        // === 已审核过的走下面 ↓
        String deleteRoles = project.getDeleteRoles();
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
/*        project.setDeleteRoles(deleteRoles);
        this.baseMapper.updateById(project);*/
        deleteRoles += uid + ",";
        LambdaUpdateWrapper<WorkExperienceAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WorkExperienceAudit::getId, id)
                .set(WorkExperienceAudit::getDeleteRoles, deleteRoles);
        this.baseMapper.update(null, updateWrapper);//只更新deleteRoles字段,不受自动填充影响
        return "删除审核记录成功";
    }

    //Dto转换
    private WorkExperienceDto getDto(WorkExperienceAudit project){
        WorkExperienceDto dto = new WorkExperienceDto();
        BeanUtils.copyProperties(project, dto);//将projectAudit的属性拷贝到dto中
        Long tid = project.getTid(); //获取tid,找到对应教师
        Long aid = project.getAid(); //获取aid,找到对应审核者id
        Teacher teacher = teacherMapper.selectById(tid);
        String teacherName = teacher.getTeacherName();
        Long oid = teacher.getOid();
        Long cid = teacher.getCid();
        dto.setTeacherName(teacherName); // set教师名称
        if (oid !=null){ // set教研室名称
            Office office = officeMapper.selectById(oid);
            if (office == null){
                log.info("oid:{}不存在,未找到教研室",oid);
                dto.setOfficeName("未找到办公室");
            }
            else{
                dto.setOfficeName(office.getOfficeName());
            }
        }
        if (cid !=null) { // set学院名称
            College college = collegeMapper.selectById(cid);
            if (college == null){
                log.info("cid:{}不存在,未找到学院",cid);
                dto.setCollegeName("未找到学院");
            }
            else{
                dto.setCollegeName(college.getCollegeName());
            }
        }
        if (aid !=null){ // set审核者名称
            if (aid.toString().startsWith("1")){
                Teacher findTeacher = teacherMapper.selectById(aid);
                if (findTeacher == null){
                    log.info("aid:{}不存在,未找到审核者",aid);
                    dto.setAuditorName("未找到审核者");
                }
                else{
                    dto.setAuditorName(findTeacher.getTeacherName());
                }
            }else {
                Admin findAdmin = adminMapper.selectById(aid);
                if (findAdmin == null){
                    log.info("aid:{}不存在,未找到审核者",aid);
                    dto.setAuditorName("未找到审核者");
                }
                else{
                    dto.setAuditorName(findAdmin.getAdminName());
                }
            }
        }

        return dto;
    }

    //检验id是否存在
    private WorkExperienceAudit checkIdExistAndReturn(Long id) {
        WorkExperienceAudit findData = this.baseMapper.selectById(id);
        if (findData == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        return findData;
    }


}
