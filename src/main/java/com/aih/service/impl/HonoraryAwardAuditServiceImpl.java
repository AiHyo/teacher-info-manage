package com.aih.service.impl;

import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.*;
import com.aih.entity.audit.HonoraryAwardAudit;
import com.aih.entity.vo.auditvo.HonoraryAwardVo;
import com.aih.mapper.*;
import com.aih.service.IHonoraryAwardAuditService;
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
 * 荣誉奖项审核 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class HonoraryAwardAuditServiceImpl extends ServiceImpl<HonoraryAwardAuditMapper, HonoraryAwardAudit> implements IHonoraryAwardAuditService {

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
    public HonoraryAwardVo queryDtoById(Long id) {
        HonoraryAwardAudit findData = this.baseMapper.selectById(id);
        if (findData == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        Long tid = findData.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }
        HonoraryAwardVo dto = this.getDto(findData);
        return dto;
    }

    @Override
    public void passHonoraryAwardAudit(HonoraryAwardAudit honoraryAward) {
        honoraryAward.setAuditStatus(1);//审核通过
        honoraryAward.setIsShow(1);//审核通过后自动显示
        this.baseMapper.updateById(honoraryAward);
    }

    @Override
    public void rejectHonoraryAwardAudit(HonoraryAwardAudit honoraryAward) {
        honoraryAward.setAuditStatus(2);//审核驳回
        this.baseMapper.updateById(honoraryAward);
    }

    @Override
    public Page<HonoraryAwardVo> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword) {
        Page<HonoraryAwardAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<HonoraryAwardVo> dtoPageInfo = new Page<>(pageNum, pageSize);

        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(HonoraryAwardAudit::getTid, uid) //查自己的
                .eq((auditStatus != null), HonoraryAwardAudit::getAuditStatus, auditStatus)
                .like((StringUtils.isNotBlank(keyword)), HonoraryAwardAudit::getContent, keyword)//模糊查询
                .orderByAsc(auditStatus==null, HonoraryAwardAudit::getAuditStatus)//先按审核状态升序 未审核=>通过=>未通过
                .orderByDesc(HonoraryAwardAudit::getCreateTime);//再按创建时间倒序
        queryWrapper.and(wrapper -> wrapper
                .notLike(HonoraryAwardAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(HonoraryAwardAudit::getDeleteRoles));
        this.baseMapper.selectPage(pageInfo,queryWrapper);
        //遍历每一条records(当前页下的所有数据)
        List<HonoraryAwardVo> collect = pageInfo.getRecords().stream().map((item) -> {
            HonoraryAwardVo dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }

    @Override
    public Page<HonoraryAwardVo> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword) {
        Page<HonoraryAwardAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<HonoraryAwardVo> dtoPageInfo = new Page<>(pageNum, pageSize);
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
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(HonoraryAwardAudit::getTid, queryTids)
                .eq((auditStatus != null), HonoraryAwardAudit::getAuditStatus, auditStatus) //0,1,2
                .ge(HonoraryAwardAudit::getCreateTime, UserInfoContext.getUser().getCreateDate().atStartOfDay())//上任以来(针对未审核的)
                .eq(onlyOwn, HonoraryAwardAudit::getAid, UserInfoContext.getUser().getId())//只看自己的(针对已审核的)
                .like((StringUtils.isNotBlank(keyword)), HonoraryAwardAudit::getContent, keyword)//模糊查询
                .orderByAsc(auditStatus==null, HonoraryAwardAudit::getAuditStatus)
                .orderByDesc(HonoraryAwardAudit::getCreateTime);//审核状态相同的则按创建时间越晚的显示在最前
        // 需要判断删除角色
        Long uid = UserInfoContext.getUser().getId();
        queryWrapper.and( wrapper -> wrapper //选出没有在删除角色中的 如果是未审核的,不允许有删除角色
                .notLike(HonoraryAwardAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(HonoraryAwardAudit::getDeleteRoles));
        //        queryWrapper.apply("academic_paper_audit.create_time <= teacher.create_date");
        this.baseMapper.selectPage(pageInfo, queryWrapper); //
        //遍历每一条records(当前页下的所有数据)
        List<HonoraryAwardVo> collect = pageInfo.getRecords().stream().map((item) -> {
            HonoraryAwardVo dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());

        //对象拷贝，拷贝一下查询到的条目数
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }


    @Override
    public void deleteOwnInfo(Long id) {
        HonoraryAwardAudit honoraryAward = this.checkIdExistAndReturn(id);
        Long uid = UserInfoContext.getUser().getId();
        Long tid = honoraryAward.getTid();
        if (!tid.equals(uid)){//只能所属教师本人可以操作
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (1 != honoraryAward.getAuditStatus()){//只能删除审核通过的
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (0 == honoraryAward.getIsShow()){//只能删除未删除的
            throw new CustomException(CustomExceptionCodeMsg.HAS_DELETE);
        }
        LambdaUpdateWrapper<HonoraryAwardAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HonoraryAwardAudit::getId, id)
                .set(HonoraryAwardAudit::getIsShow, 0);
        this.baseMapper.update(null, updateWrapper);//只更新isShow字段,不受自动填充影响
    }


    @Override
    public String deleteRecord(Long id)
    {
        HonoraryAwardAudit honoraryAward = this.checkIdExistAndReturn(id);
        log.info("find honoraryAward:{}", honoraryAward);
        Long tid = honoraryAward.getTid();
        Long uid = UserInfoContext.getUser().getId();
        //特判：未审核的记录,直接正常删除
        if (0 == honoraryAward.getAuditStatus()){
            //只能所属教师本人可以操作
            log.info("auditStatus=0,tid:{},uid:{}", tid, uid);
            if (!Objects.equals(tid, uid)){
                throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
            }
            this.baseMapper.deleteById(id);
            return "删除审核申请成功";
        }
        // === 已审核过的走下面 ↓
        String deleteRoles = honoraryAward.getDeleteRoles();
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
/*        honoraryAward.setDeleteRoles(deleteRoles);
        this.baseMapper.updateById(honoraryAward);*/
        deleteRoles += uid + ",";
        LambdaUpdateWrapper<HonoraryAwardAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(HonoraryAwardAudit::getId, id)
                .set(HonoraryAwardAudit::getDeleteRoles, deleteRoles);
        this.baseMapper.update(null, updateWrapper);//只更新deleteRoles字段,不受自动填充影响
        return "删除审核记录成功";
    }

    //Dto转换
    @Override
    public HonoraryAwardVo getDto(HonoraryAwardAudit honoraryAward){
        HonoraryAwardVo dto = new HonoraryAwardVo();
        BeanUtils.copyProperties(honoraryAward, dto);//将honoraryAwardAudit的属性拷贝到dto中
        Long tid = honoraryAward.getTid(); //获取tid,找到对应教师
        Long aid = honoraryAward.getAid(); //获取aid,找到对应审核者id
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
    private HonoraryAwardAudit checkIdExistAndReturn(Long id) {
        HonoraryAwardAudit findData = this.baseMapper.selectById(id);
        if (findData == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        return findData;
    }


}
