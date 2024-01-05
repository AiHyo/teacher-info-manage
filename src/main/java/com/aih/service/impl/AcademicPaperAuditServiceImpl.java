package com.aih.service.impl;

import com.aih.entity.*;
import com.aih.entity.vo.audit.AcademicPaperDto;
import com.aih.mapper.*;
import com.aih.service.IAcademicPaperAuditService;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
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
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CollegeMapper collegeMapper;


    @Override
    public AcademicPaperDto queryDtoById(Long id) {
        AcademicPaperAudit findData = this.baseMapper.selectById(id);
        if (findData == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        Long tid = findData.getTid();
        List<Long> powerIds = myUtil.getPowerIdsByTid(tid);
        if (!powerIds.contains(UserInfoContext.getUser().getId())){
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_QUERY);
        }
        AcademicPaperDto dto = this.getDto(findData);
        return dto;
    }

    @Override
    public void passAcademicPaperAudit(AcademicPaperAudit academicPaper) {
        academicPaper.setAuditStatus(1);//审核通过
        academicPaper.setIsShow(1);//审核通过后自动显示
        this.baseMapper.updateById(academicPaper);
    }

    @Override
    public void rejectAcademicPaperAudit(AcademicPaperAudit academicPaper) {
        academicPaper.setAuditStatus(2);//审核驳回
        this.baseMapper.updateById(academicPaper);
    }

    @Override
    public Page<AcademicPaperDto> queryOwnRecord(Integer pageNum, Integer pageSize, Integer auditStatus, String keyword) {
        Page<AcademicPaperAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<AcademicPaperDto> dtoPageInfo = new Page<>(pageNum, pageSize);

        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AcademicPaperAudit::getTid, uid) //查自己的
                .eq((auditStatus != null), AcademicPaperAudit::getAuditStatus, auditStatus)
                .like((StringUtils.isNotBlank(keyword)), AcademicPaperAudit::getTitle, keyword)//模糊查询
                .orderByAsc(auditStatus!=null, AcademicPaperAudit::getAuditStatus)//先按审核状态升序 未审核=>通过=>未通过
                .orderByDesc(AcademicPaperAudit::getCreateTime);//再按创建时间倒序
        if (auditStatus != null && auditStatus != 0){
            queryWrapper.and( wrapper -> wrapper
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }
        this.baseMapper.selectPage(pageInfo,queryWrapper);
        //遍历每一条records(当前页下的所有数据)
        List<AcademicPaperDto> collect = pageInfo.getRecords().stream().map((item) -> {
            AcademicPaperDto dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }

    @Override
    public Page<AcademicPaperDto> queryPowerRecords(Integer pageNum, Integer pageSize, Integer auditStatus, Boolean onlyOwn, String keyword) {
        Page<AcademicPaperAudit> pageInfo = new Page<>(pageNum, pageSize);
        Page<AcademicPaperDto> dtoPageInfo = new Page<>(pageNum, pageSize);
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
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(AcademicPaperAudit::getTid, queryTids)
                .eq((auditStatus != null), AcademicPaperAudit::getAuditStatus, auditStatus) //0,1,2
                .ge(AcademicPaperAudit::getCreateTime, UserInfoContext.getUser().getCreateDate().atStartOfDay())//上任以来(针对未审核的)
                .eq(onlyOwn, AcademicPaperAudit::getAid, UserInfoContext.getUser().getId())//只看自己的(针对已审核的)
                .like(StringUtils.isNotBlank(keyword), AcademicPaperAudit::getTitle, keyword)//模糊查询[标题]
                .orderByAsc(auditStatus==null, AcademicPaperAudit::getAuditStatus)
                .orderByDesc(AcademicPaperAudit::getCreateTime);//审核状态相同的按创建时间越晚的显示在最前
        // 删除过记录的情况：需要判断删除角色
        if (auditStatus != null && auditStatus != 0){
            Long uid = UserInfoContext.getUser().getId();
            queryWrapper.and( wrapper -> wrapper //选出没有在删除角色中的 如果是未审核的,不允许有删除角色
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }
//        queryWrapper.apply("academic_paper_audit.create_time <= teacher.create_date");
        this.baseMapper.selectPage(pageInfo, queryWrapper); //
        //遍历每一条records(当前页下的所有数据)
        List<AcademicPaperDto> collect = pageInfo.getRecords().stream().map((item) -> {
            AcademicPaperDto dto = this.getDto(item);
            return dto;
        }).collect(Collectors.toList());

        //对象拷贝，拷贝一下查询到的条目数
        BeanUtils.copyProperties(pageInfo, dtoPageInfo, "records");//拷贝除了records的属性
        dtoPageInfo.setRecords(collect);//将collect赋值给records
        return dtoPageInfo;
    }

    @Override
    public void deleteOwnInfo(Long id) {
        AcademicPaperAudit academicPaper = this.checkIdExistAndReturn(id);
        Long uid = UserInfoContext.getUser().getId();
        Long tid = academicPaper.getTid();
        if (!tid.equals(uid)){//只能所属教师本人可以操作
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (1 != academicPaper.getAuditStatus()){//只能删除审核通过的
            throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
        }
        if (0 == academicPaper.getIsShow()){//只能删除未删除的
            throw new CustomException(CustomExceptionCodeMsg.HAS_DELETE);
        }
        LambdaUpdateWrapper<AcademicPaperAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AcademicPaperAudit::getId, id)
                .set(AcademicPaperAudit::getIsShow, 0);
        this.baseMapper.update(null, updateWrapper);//只更新isShow字段,不受自动填充影响
    }


    @Override
    public String deleteRecord(Long id)
    {
        AcademicPaperAudit academicPaper = this.checkIdExistAndReturn(id);
        log.info("find academicPaper:{}", academicPaper);
        Long tid = academicPaper.getTid();
        Long uid = UserInfoContext.getUser().getId();
        //特判：未审核的记录,直接正常删除
        if (0 == academicPaper.getAuditStatus()){
            //只能所属教师本人可以操作
            log.info("auditStatus=0,tid:{},uid:{}", tid, uid);
            if (!Objects.equals(tid, uid)){
                throw new CustomException(CustomExceptionCodeMsg.NO_POWER_DELETE);
            }
            this.baseMapper.deleteById(id);
            return "删除审核申请成功";
        }
        // === 已审核过的走下面 ↓
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
/*        academicPaper.setDeleteRoles(deleteRoles);
        this.baseMapper.updateById(academicPaper);*/
        deleteRoles += uid + ",";
        LambdaUpdateWrapper<AcademicPaperAudit> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AcademicPaperAudit::getId, id)
                .set(AcademicPaperAudit::getDeleteRoles, deleteRoles);
        this.baseMapper.update(null, updateWrapper);//只更新deleteRoles字段,不受自动填充影响
        return "删除审核记录成功";
    }



    //封装一个传入AcaPaperAudit返回AcaPaperDto的方法
    private AcademicPaperDto getDto(AcademicPaperAudit academicPaper){
        AcademicPaperDto dto = new AcademicPaperDto();
        BeanUtils.copyProperties(academicPaper, dto);//将academicPaperAudit的属性拷贝到dto中
        Long tid = academicPaper.getTid(); //获取tid,找到对应教师
        Long aid = academicPaper.getAid(); //获取aid,找到对应审核者id
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
    private AcademicPaperAudit checkIdExistAndReturn(Long id){
        AcademicPaperAudit findData = this.baseMapper.selectById(id);
        if (findData == null){
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        return findData;

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
