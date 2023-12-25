package com.aih.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aih.entity.*;
import com.aih.entity.vo.AuditInfoDto;
import com.aih.entity.vo.RequestCollegeChangeDto;
import com.aih.entity.vo.TeacherDto;
import com.aih.mapper.*;
import com.aih.service.IAdminService;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private RequestCollegeChangeMapper requestCollegeChangeMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUtil myUtil;
    @Autowired
    private AcademicPaperAuditServiceImpl academicPaperService;


    @Override
    public Map<String, Object> login(Admin admin) {
        //根据教师用户名查询
        log.info("admin传入参数:{}",admin);
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,admin.getUsername());
        Admin loginAdmin = this.baseMapper.selectOne(queryWrapper);
        log.info("找到的loginAdmin:{}",loginAdmin);
        //如果不为空,并且密码和传入密码相匹配,则生成token
        if(loginAdmin!=null && passwordEncoder.matches(admin.getPassword(),loginAdmin.getPassword()))
        {
            if (loginAdmin.getStatus() == 0) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_DISABLED);
            }
            loginAdmin.setPassword(null);//清空密码
            String token = jwtUtil.createToken(loginAdmin,"Admin");
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    @Override
    public Admin showInfo() {
        Long id = UserInfoContext.getUser().getId();
        /*if(!String.valueOf(id).startsWith("2")){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
        }*/
        Admin loginAdmin = this.baseMapper.selectById(id);
        loginAdmin.setPassword(null);//将密码置空
        return loginAdmin;
    }

    @Override
    public void updateIsAuditor(Integer isAuditor, List<Long> ids) {
        log.info("isAuditor:{},ids:{}",isAuditor,ids);
        //获取 isAuditor 变了的 new_ids, 只修改变动的,因为会修改 权限生效时间createDate
        List<Long> new_ids = ids.stream().filter(id -> {
            Teacher teacher = teacherMapper.selectById(id);
            return !teacher.getIsAuditor().equals(isAuditor); // 返回不等于isAuditor的id(即需要修改的)
        }).collect(Collectors.toList());
        if (new_ids.isEmpty()) {
            return;
        }
        //开始操作
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Teacher::getId, new_ids)
                .set(Teacher::getIsAuditor, isAuditor)
                .set(Teacher::getCreateDate, LocalDate.now());//修改角色权限之后新的创建时间
        teacherMapper.update(null,updateWrapper);//null表示不修改其他字段
    }

    @Override
    public void resetPassword(List<Long> ids, String password) {
        log.info("重置密码的ids:{}",ids);
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Teacher::getId,ids)
                .set(Teacher::getPassword, passwordEncoder.encode(password));
        teacherMapper.update(null,updateWrapper);
    }

    @Override
    public Page<AuditInfoDto> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn) {
        Long uid = UserInfoContext.getUser().getId();
        Admin cur_admin = this.baseMapper.selectById(uid);
        //获取管理员有审核权限的tidList
        List<Long> adminCanAuditTids = myUtil.getAdminCanAuditTids();
        if(adminCanAuditTids.isEmpty()) {
            return new Page<>();
        }
        //封装所有的审核信息
        ArrayList<AuditInfoDto> all_auditInfoDtoList = new ArrayList<>();
        // === AcademicPaperAudit ===
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper
                .in(AcademicPaperAudit::getTid, adminCanAuditTids)
                .eq(auditStatus!=null,AcademicPaperAudit::getAuditStatus, auditStatus)
                .ge(AcademicPaperAudit::getCreateTime, cur_admin.getCreateDate())//上任以来(针对未审核的)
                .eq(onlyOwn, AcademicPaperAudit::getAid, cur_admin.getId());//只看自己审核的(针对审核过的)
        if (auditStatus != null && auditStatus != 0){//选出没有在删除角色中的
            queryWrapper_academicPaper.and( wrapper -> wrapper
                    .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                    .or().isNull(AcademicPaperAudit::getDeleteRoles));
        }

        List<AcademicPaperAudit> academicPaperAuditList = academicPaperService.list(queryWrapper_academicPaper);
        log.info("academicPaperAuditList={}", academicPaperAuditList);
        List<AuditInfoDto> one_auditInfoDtoList = academicPaperAuditList.stream().map((item -> {
            AuditInfoDto dto = new AuditInfoDto();
            dto.setAuditType("论文材料");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = teacherMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(this.baseMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoDtoList.addAll(one_auditInfoDtoList);
        // === EducationExperienceAudit ===
        // ……………………………………………………………………………………
        // === OtherAudit ===
        // === OtherAudit ===
        // === OtherAudit ===
        // === OtherAudit ===


        Page<AuditInfoDto> pageInfo = new Page<>(pageNum, pageSize);
        pageInfo.setRecords(all_auditInfoDtoList);
        pageInfo.setTotal(all_auditInfoDtoList.size());
        return pageInfo;
    }

    @Override
    public Page<RequestCollegeChangeDto> getChangeCollegeRequestList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn) {
        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<RequestCollegeChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RequestCollegeChange::getOldCid,UserInfoContext.getUser().getCid())
                .eq(auditStatus!=null,RequestCollegeChange::getAuditStatus,auditStatus)
                .eq( onlyOwn, RequestCollegeChange::getOldAid, uid); // 只看自己审核的
        if (auditStatus != null && auditStatus != 0){//选出没有在删除角色中的
            queryWrapper.and( wrapper -> wrapper
                    .notLike(RequestCollegeChange::getDeleteRoles, "," + uid + ",")
                    .or().isNull(RequestCollegeChange::getDeleteRoles));
        }
        Page<RequestCollegeChange> page = new Page<>(pageNum, pageSize);
        requestCollegeChangeMapper.selectPage(page,queryWrapper);
        List<RequestCollegeChangeDto> collect = page.getRecords().stream().map((item -> {
            RequestCollegeChangeDto requestCollegeChangeDto = new RequestCollegeChangeDto();
            BeanUtils.copyProperties(item, requestCollegeChangeDto);
            requestCollegeChangeDto.setTeacherName(teacherMapper.getTeacherNameByTid(item.getTid()));
            requestCollegeChangeDto.setOldCollegeName(collegeMapper.getCollegeNameByCid(item.getOldCid()));
            requestCollegeChangeDto.setOldAdminName(this.baseMapper.getAdminNameByAid(item.getOldAid()));
            requestCollegeChangeDto.setNewCollegeName(collegeMapper.getCollegeNameByCid(item.getNewCid()));
            requestCollegeChangeDto.setNewAdminName(this.baseMapper.getAdminNameByAid(item.getNewAid()));
            return requestCollegeChangeDto;
        })).collect(Collectors.toList());
        Page<RequestCollegeChangeDto> dtoPage = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(page,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public Page<RequestCollegeChangeDto> getChangeCollegeAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn) {
        Long uid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<RequestCollegeChange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RequestCollegeChange::getNewCid,UserInfoContext.getUser().getCid())
                .eq(auditStatus!=null,RequestCollegeChange::getAuditStatus,auditStatus)
                .eq( onlyOwn, RequestCollegeChange::getNewAid, uid); // 只看自己审核的
        if (auditStatus != null && auditStatus != 0){//选出没有在删除角色中的
            queryWrapper.and( wrapper -> wrapper
                    .notLike(RequestCollegeChange::getDeleteRoles, "," + uid + ",")
                    .or().isNull(RequestCollegeChange::getDeleteRoles));
        }
        Page<RequestCollegeChange> page = new Page<>(pageNum, pageSize);
        requestCollegeChangeMapper.selectPage(page,queryWrapper);
        List<RequestCollegeChangeDto> collect = page.getRecords().stream().map((item -> {
            RequestCollegeChangeDto dto = new RequestCollegeChangeDto();
            BeanUtils.copyProperties(item, dto);
            dto.setTeacherName(teacherMapper.getTeacherNameByTid(item.getTid()));
            dto.setOldCollegeName(collegeMapper.getCollegeNameByCid(item.getOldCid()));
            dto.setOldAdminName(this.baseMapper.getAdminNameByAid(item.getOldAid()));
            dto.setNewCollegeName(collegeMapper.getCollegeNameByCid(item.getNewCid()));
            dto.setNewAdminName(this.baseMapper.getAdminNameByAid(item.getNewAid()));
            return dto;
        })).collect(Collectors.toList());
        Page<RequestCollegeChangeDto> dtoPage = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(page,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public void applyChangeTeacherCollege(Long tid, Long newCid, String oldAdminRemark) {
        Long oldCid = UserInfoContext.getUser().getCid();
        //创建申请
        RequestCollegeChange requestCollegeChange = new RequestCollegeChange();
        requestCollegeChange.setAuditStatus(0);
        requestCollegeChange.setTid(tid);
        requestCollegeChange.setNewCid(newCid);
        requestCollegeChange.setOldCid(oldCid);
        requestCollegeChange.setOldAid(UserInfoContext.getUser().getId());
        requestCollegeChange.setCreateTime(LocalDateTime.now());
        requestCollegeChange.setDeleteRoles(",");
        if (StrUtil.isNotBlank(oldAdminRemark)) {
            requestCollegeChange.setOldAdminRemark(oldAdminRemark);
        }
        requestCollegeChangeMapper.insert(requestCollegeChange);
    }

    @Override
    public void passChangeCollege(RequestCollegeChange requestCollegeChange, String newAdminRemark) {
        Long newCid = requestCollegeChange.getNewCid();
        //通过申请
        requestCollegeChange.setAuditStatus(1);
        requestCollegeChange.setAuditTime(LocalDateTime.now());
        requestCollegeChange.setNewAid(UserInfoContext.getUser().getId());
        if (StrUtil.isNotBlank(newAdminRemark)) {
            requestCollegeChange.setNewAdminRemark(newAdminRemark);
        }
        requestCollegeChangeMapper.updateById(requestCollegeChange);
        //修改教师的cid
        Teacher teacher = teacherMapper.selectById(requestCollegeChange.getTid());
        teacher.setCid(newCid);
        teacher.setOid(0L);//换学院后,教师的教研室置空
        teacherMapper.updateById(teacher);
    }

    @Override
    public void refuseChangeCollege(RequestCollegeChange requestCollegeChange, String newAdminRemark) {
        requestCollegeChange.setAuditStatus(2);
        requestCollegeChange.setAuditTime(LocalDateTime.now());
        requestCollegeChange.setNewAid(UserInfoContext.getUser().getId());
        if (StrUtil.isNotBlank(newAdminRemark)) {
            requestCollegeChange.setNewAdminRemark(newAdminRemark);
        }
        requestCollegeChangeMapper.updateById(requestCollegeChange);
    }

    @Override
    public String deleteChangeCollege(Long id) {
        RequestCollegeChange requestCollegeChange = requestCollegeChangeMapper.selectById(id);
        if (requestCollegeChange == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_REQUEST_COLLEGE_CHANGE);
        }
        //判断是否是oldAid或者newAid
        Long oldAid = requestCollegeChange.getOldAid();
        Long newAid = requestCollegeChange.getNewAid();
        Long uid = UserInfoContext.getUser().getId();
        if (!oldAid.equals(uid) && !newAid.equals(uid)) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        //如果是待审核,只有oldAid可以,并且直接删除该申请记录
        if (requestCollegeChange.getAuditStatus() == 0) {
            if (!oldAid.equals(uid)) {
                throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
            }
            requestCollegeChangeMapper.deleteById(id);
            return "删除申请成功";
        }
        //如果是已审核,都可以,但是只进行添加删除角色功能
        String deleteRoles = requestCollegeChange.getDeleteRoles();
        //已经删除过:
        if (deleteRoles.contains("," + uid + ",")){
            throw new CustomException(CustomExceptionCodeMsg.HAS_DELETE);
        }
        deleteRoles += uid + ",";
        requestCollegeChange.setDeleteRoles(deleteRoles);
        requestCollegeChangeMapper.updateById(requestCollegeChange);//换学院申请实体类没有设置自动填充,可以直接在实体类update
        return "删除申请记录成功";
    }

    @Override
    public void deleteTeacher(List<Long> ids) {
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void logout() {

    }

    @Override
    public Page<TeacherDto> getTeacherList(Integer pageNum, Integer pageSize, String keyword, Long oid, String officeName, String teacherName, Integer isAuditor, Integer gender, String ethnic, String birthplace, String address) {
        log.debug("进入getTeacherListServiceImpl");
        Page<Teacher> pageInfo = new Page<>(pageNum, pageSize);
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getCid,cid);//对应学院下的所有
        queryWrapper
                .eq(oid!=null,Teacher::getOid,oid)
                .eq(isAuditor!=null,Teacher::getIsAuditor,isAuditor)
                .eq(gender!=null,Teacher::getGender,gender)
                .like(StringUtils.isNotBlank(ethnic),Teacher::getEthnic,ethnic)
                .like(StringUtils.isNotBlank(birthplace),Teacher::getNativePlace,birthplace)
                .like(StringUtils.isNotBlank(address),Teacher::getAddress,address)
                .orderByAsc(Teacher::getOid);//根据教研室排序
        if (StringUtils.isBlank(keyword)) {//如果keyword为空,不用再加条件
            teacherMapper.selectPage(pageInfo,queryWrapper);
        }
        else{
            // keyword 教研室名称 / 教师姓名
            List<Long> oids = null;
            LambdaQueryWrapper<Office> officeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            officeLambdaQueryWrapper.like(Office::getOfficeName,keyword); // 教研室名称
            List<Office> officeList = officeMapper.selectList(officeLambdaQueryWrapper);
            if (officeList.size()>0) { // 如果有教研室名称匹配的, 存入教研室id: oids
                oids = officeList.stream()
                        .map(Office::getId)
                        .collect(Collectors.toList());
            }
            final List<Long> finalOids = oids; // in中的参数须为final
            queryWrapper.and(wrapper -> wrapper
                    .in(finalOids !=null,Teacher::getOid, finalOids)
//                        .or().like(StringUtils.isNotBlank(keyword),Teacher::getTeacherName,keyword));
                    .or().like(Teacher::getTeacherName,keyword));
            teacherMapper.selectPage(pageInfo,queryWrapper);
        }
        if (StrUtil.isNotBlank(officeName)) {
            pageInfo.getRecords().removeIf(teacher -> !officeMapper.getOfficeNameByOid(teacher.getOid()).contains(officeName));//如果不包含officeName,就移除
        }
        if (StrUtil.isNotBlank(teacherName)) {
            pageInfo.getRecords().removeIf(teacher -> !teacher.getTeacherName().contains(teacherName));//如果不包含teacherName,就移除
        }
        List<TeacherDto> collect = pageInfo.getRecords().stream().map((item -> {
            TeacherDto teacherDto = new TeacherDto();
            BeanUtils.copyProperties(item, teacherDto);//复制item给TeacherDto
            teacherDto.setOfficeName(officeMapper.getOfficeNameByOid(item.getOid()));
            teacherDto.setCollegeName(collegeMapper.getCollegeNameByCid(item.getCid()));
            List<String> roleNameByTeacherId = teacherMapper.getRoleNameByTeacherId(item.getId());
            teacherDto.setRoleList(String.join(",", roleNameByTeacherId));
            return teacherDto;
        })).collect(Collectors.toList());
        Page<TeacherDto> dtoPage = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }



}
