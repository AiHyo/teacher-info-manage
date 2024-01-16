package com.aih.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.audit.*;
import com.aih.entity.vo.*;
import com.aih.entity.vo.auditvo.*;
import com.aih.entity.vo.export.excel.TeacherExcelModel;
import com.aih.entity.vo.export.word.TeacherWordModel;
import com.aih.entity.vo.export.word.table.*;
import com.aih.service.*;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.*;
import com.aih.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.SheetUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 教师(用户) 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUtil myUtil;
    @Resource
    private IdentityCardAuditMapper identityCardAuditMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private IEducationExperienceAuditService educationExperienceService;
    @Autowired
    private IWorkExperienceAuditService workExperienceService;
    @Autowired
    private IHonoraryAwardAuditService honoraryAwardService;
    @Autowired
    private ITopicAuditService topicService;
    @Autowired
    private IAcademicPaperAuditService academicPaperService;
    @Autowired
    private IProjectAuditService projectService;
    @Autowired
    private ISoftwareAuditService softwareService;

    @Value("${default-password}")
    String defaultPassword;
    @Value("${excel.file-name}")
    String defaultExcelName;
    @Value("${file.root-path}")
    String rootPath;
    @Value("${file.template-file-word}")
    String wordTemplateFile;
    @Value("${file.temporary-path}")
    String temporaryPath;
    private final String sep = File.separator;

    @Override
    public Map<String, Object> login(Teacher teacher) {
        //根据教师用户名查询
        log.info("teacher传入参数:{}",teacher);
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getUsername,teacher.getUsername());
        Teacher loginTeacher = this.baseMapper.selectOne(queryWrapper);
        log.info("找到的loginTeacher:{}",loginTeacher);
        //如果不为空,并且密码和传入密码相匹配,则生成token
        if(loginTeacher!=null && passwordEncoder.matches(teacher.getPassword(),loginTeacher.getPassword()))
        {
            loginTeacher.setPassword(null);//将密码置空
            String token = jwtUtil.createToken(loginTeacher,"Teacher");
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            data.put("isDefault",teacher.getPassword().equals(defaultPassword));//是否为默认密码
            data.put("teacherName",loginTeacher.getTeacherName());
            return data;
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    @Override//根据tid获取教师信息TeacherDto的
    public TeacherDetailVo queryTeacherDetailDtoByTid(Long tid) {
        Teacher findTeacher = this.baseMapper.selectById(tid);
        findTeacher.setPassword(null);//将密码置空
        TeacherDetailVo teacherDetailVo = new TeacherDetailVo();
        BeanUtils.copyProperties(findTeacher, teacherDetailVo);//将findTeacher的属性复制到teacherDto
        //获取学院和科室名称
        teacherDetailVo.setCollegeName(collegeMapper.getCollegeNameByCid(findTeacher.getCid()));
        teacherDetailVo.setOfficeName(officeMapper.getOfficeNameByOid(findTeacher.getOid()));
        //获取职务
        List<String> roleNameByTeacherId = this.baseMapper.getRoleNameByTeacherId(tid);
        teacherDetailVo.setRoleList(String.join(",", roleNameByTeacherId));
/*        //获取正在显示的身份证资料
        teacherDetailDto.setIdentityCard(this.queryIdentityCardShowByTid(tid));*/
        //教育经历
        teacherDetailVo.setEducationExperienceList(this.queryEducationExperienceShowListByTid(tid));
        //工作经历
        teacherDetailVo.setWorkExperienceList(this.queryWorkExperienceShowListByTid(tid));
        //荣誉奖励
        teacherDetailVo.setHonoraryAwardList(this.queryHonoraryAwardShowListByTid(tid));
        //课题
        teacherDetailVo.setTopicList(this.queryTopicShowListByTeacherId(tid));
        //论文
        teacherDetailVo.setAcademicPaperList(this.queryAcademicPaperShowListByTeacherId(tid));
        //项目
        teacherDetailVo.setProjectList(this.queryProjectShowListByTeacherId(tid));
        //软件著作
        teacherDetailVo.setSoftwareList(this.querySoftwareShowListByTeacherId(tid));
        return teacherDetailVo;
    }

    @Override
    public void logout() {
        //退出登录,将token加入黑名单
    }

    @Override
    public Page<TeacherVo> getTeacherList(Integer pageNum, Integer pageSize, String teacherName, Integer gender, String ethnic, String birthplace, String address) {
        Page<Teacher> pageInfo = new Page<>(pageNum, pageSize);
        Long oid = UserInfoContext.getUser().getOid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getOid,oid);//对应办公室下的所有
        queryWrapper.ne(Teacher::getId, UserInfoContext.getUser().getId());//排除id为自己的
        queryWrapper.like(StringUtils.isNotBlank(teacherName),Teacher::getTeacherName,teacherName);
        queryWrapper.eq(gender != null,Teacher::getGender,gender);
        queryWrapper.like(StringUtils.isNotBlank(ethnic),Teacher::getEthnic,ethnic);
        queryWrapper.like(StringUtils.isNotBlank(birthplace),Teacher::getNativePlace,birthplace);
        queryWrapper.like(StringUtils.isNotBlank(address),Teacher::getAddress,address);
        this.baseMapper.selectPage(pageInfo,queryWrapper);
        List<TeacherVo> collect = pageInfo.getRecords().stream().map((item) -> {
            TeacherVo teacherDto = new TeacherVo();
            BeanUtils.copyProperties(item, teacherDto);
            teacherDto.setCollegeName(collegeMapper.getCollegeNameByCid(item.getCid()));
            teacherDto.setOfficeName(officeMapper.getOfficeNameByOid(item.getOid()));
            teacherDto.setRoleList(String.join(",", this.baseMapper.getRoleNameByTeacherId(item.getId())));
            return teacherDto;
        }).collect(Collectors.toList());
        Page<TeacherVo> pageDtoInfo = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        pageDtoInfo.setRecords(collect);
        return pageDtoInfo;
    }

    @Override
    public Page<AuditInfoVo> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn) {
        Long uid = UserInfoContext.getUser().getId();
        Teacher cur_teacher = this.baseMapper.selectById(uid);
        LocalDate createDate = cur_teacher.getCreateDate();
        //获取审核员有权限审核的tidList
        List<Long> auditorCanAuditTids = myUtil.getAuditorCanAuditTids();
        if(auditorCanAuditTids.isEmpty()){
            return null;
        }
        //获取每类审核信息
        List<AuditInfoVo> all_auditInfoVoList = this.getAllAuditInfoDtoList(auditorCanAuditTids,auditStatus,onlyOwn,uid,createDate);
        Page<AuditInfoVo> pageInfo = new Page<>(pageNum, pageSize);
        pageInfo.setRecords(all_auditInfoVoList);
        pageInfo.setTotal(all_auditInfoVoList.size());
        return pageInfo;
    }

    @Override
    public List<AuditInfoVo> getAllAuditInfoDtoList(List<Long> auditorCanAuditTids, Integer auditStatus, boolean onlyOwn, Long uid, LocalDate createDate) {
        List<AuditInfoVo> all_auditInfoVoList = new ArrayList<>();
        // === AcademicPaperAudit ===
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper
                .in(AcademicPaperAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,AcademicPaperAudit::getAuditStatus, auditStatus)
                .ge(AcademicPaperAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,AcademicPaperAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_academicPaper.and( wrapper -> wrapper
                .notLike(AcademicPaperAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(AcademicPaperAudit::getDeleteRoles));
        List<AcademicPaperAudit> academicPaperAuditList = academicPaperService.list(queryWrapper_academicPaper);
        log.info("academicPaperAuditList={}", academicPaperAuditList);
        List<AuditInfoVo> one_auditInfoVoList = academicPaperAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("论文材料");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(one_auditInfoVoList);
        // === EducationExperienceAudit ===
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper_educationExperience = new LambdaQueryWrapper<>();
        queryWrapper_educationExperience
                .in(EducationExperienceAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,EducationExperienceAudit::getAuditStatus, auditStatus)
                .ge(EducationExperienceAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,EducationExperienceAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_educationExperience.and( wrapper -> wrapper
                .notLike(EducationExperienceAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(EducationExperienceAudit::getDeleteRoles));
        List<EducationExperienceAudit> educationExperienceAuditList = educationExperienceService.list(queryWrapper_educationExperience);
        log.info("educationExperienceAuditList={}", educationExperienceAuditList);
        List<AuditInfoVo> two_auditInfoVoList = educationExperienceAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("教育经历");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(two_auditInfoVoList);
        // === HonoraryAwardAudit ===
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper_honoraryAward = new LambdaQueryWrapper<>();
        queryWrapper_honoraryAward
                .in(HonoraryAwardAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,HonoraryAwardAudit::getAuditStatus, auditStatus)
                .ge(HonoraryAwardAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,HonoraryAwardAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_honoraryAward.and( wrapper -> wrapper
                .notLike(HonoraryAwardAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(HonoraryAwardAudit::getDeleteRoles));
        List<HonoraryAwardAudit> honoraryAwardAuditList = honoraryAwardService.list(queryWrapper_honoraryAward);
        log.info("honoraryAwardAuditList={}", honoraryAwardAuditList);
        List<AuditInfoVo> three_auditInfoVoList = honoraryAwardAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("荣誉奖励");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(three_auditInfoVoList);
        // === ProjectAudit ===
        LambdaQueryWrapper<ProjectAudit> queryWrapper_project = new LambdaQueryWrapper<>();
        queryWrapper_project
                .in(ProjectAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,ProjectAudit::getAuditStatus, auditStatus)
                .ge(ProjectAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,ProjectAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_project.and( wrapper -> wrapper
                .notLike(ProjectAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(ProjectAudit::getDeleteRoles));
        List<ProjectAudit> projectAuditList = projectService.list(queryWrapper_project);
        log.info("projectAuditList={}", projectAuditList);
        List<AuditInfoVo> four_auditInfoVoList = projectAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("项目");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(four_auditInfoVoList);
        // === SoftwareAudit ===
        LambdaQueryWrapper<SoftwareAudit> queryWrapper_software = new LambdaQueryWrapper<>();
        queryWrapper_software
                .in(SoftwareAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,SoftwareAudit::getAuditStatus, auditStatus)
                .ge(SoftwareAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,SoftwareAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_software.and( wrapper -> wrapper
                .notLike(SoftwareAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(SoftwareAudit::getDeleteRoles));
        List<SoftwareAudit> softwareAuditList = softwareService.list(queryWrapper_software);
        log.info("softwareAuditList={}", softwareAuditList);
        List<AuditInfoVo> five_auditInfoVoList = softwareAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("软件著作");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(five_auditInfoVoList);
        // === TopicAudit ===
        LambdaQueryWrapper<TopicAudit> queryWrapper_topic = new LambdaQueryWrapper<>();
        queryWrapper_topic
                .in(TopicAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,TopicAudit::getAuditStatus, auditStatus)
                .ge(TopicAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,TopicAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_topic.and( wrapper -> wrapper
                .notLike(TopicAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(TopicAudit::getDeleteRoles));
        List<TopicAudit> topicAuditList = topicService.list(queryWrapper_topic);
        log.info("topicAuditList={}", topicAuditList);
        List<AuditInfoVo> six_auditInfoVoList = topicAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("课题");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(six_auditInfoVoList);
        // === WorkExperienceAudit ===
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper_workExperience = new LambdaQueryWrapper<>();
        queryWrapper_workExperience
                .in(WorkExperienceAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,WorkExperienceAudit::getAuditStatus, auditStatus)
                .ge(WorkExperienceAudit::getCreateTime, createDate)//上任以来(针对未审核的)
                .eq(onlyOwn,WorkExperienceAudit::getTid, uid);//只看自己的(针对已审核的)
        //选出没有在删除角色中的
        queryWrapper_workExperience.and( wrapper -> wrapper
                .notLike(WorkExperienceAudit::getDeleteRoles, "," + uid + ",")
                .or().isNull(WorkExperienceAudit::getDeleteRoles));
        List<WorkExperienceAudit> workExperienceAuditList = workExperienceService.list(queryWrapper_workExperience);
        log.info("workExperienceAuditList={}", workExperienceAuditList);
        List<AuditInfoVo> seven_auditInfoVoList = workExperienceAuditList.stream().map((item -> {
            AuditInfoVo dto = new AuditInfoVo();
            dto.setAuditType("工作经历");
            dto.setId(item.getId());
            Integer _auditStatus = item.getAuditStatus();
            dto.setAuditStatus(_auditStatus == 1 ? "审核通过": _auditStatus == 2 ? "审核未通过": "待审核");
            dto.setCreateTime(item.getCreateTime());
            dto.setAuditTime(item.getAuditTime());
            Teacher teacher = this.baseMapper.selectById(item.getTid());
            if (teacher != null) {
                dto.setTeacherName(teacher.getTeacherName());
                dto.setOfficeName(officeMapper.getOfficeNameByOid(teacher.getOid()));
                dto.setCollegeName(collegeMapper.getCollegeNameByCid(teacher.getCid()));
            }
            dto.setAuditName(adminMapper.getAdminNameByAid(item.getAid()));
            return dto;
        })).collect(Collectors.toList());
        all_auditInfoVoList.addAll(seven_auditInfoVoList);
        return all_auditInfoVoList;
    }

    @Override
    public List<Map<String, Object>> getTeacherCountByCollege() {
        List<College> colleges = collegeMapper.selectList(null);
        List<Map<String, Object>> collect = colleges.stream().map((item) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("collegeName", item.getCollegeName());
            map.put("teacherCount", this.baseMapper.selectCount(new LambdaQueryWrapper<Teacher>().eq(Teacher::getCid, item.getId())));
            return map;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean checkUsernameExist(String username) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getUsername, username);
        return this.baseMapper.selectCount(queryWrapper) > 0;
    }


    @Override
    public List<String> getRoleListByTid(Long id) {
        return this.baseMapper.getRoleNameByTeacherId(id);
    }

    @Override
    public XWPFTemplate getWordRenderByTid(Long tid) throws IOException {
        //获取数据
        TeacherWordModel wordModel = new TeacherWordModel(this.queryTeacherDetailDtoByTid(tid));
        Map<String, Object> map = new HashMap<>();
        map.put("teacherName", wordModel.getTeacherName());
        map.put("gender", wordModel.getGender());
        map.put("politicsStatus", wordModel.getPoliticsStatus());
        map.put("ethnic", wordModel.getEthnic());
        map.put("nativePlace", wordModel.getNativePlace());
        map.put("educationDegree", wordModel.getEducationDegree());
        map.put("collegeName", wordModel.getCollegeName());
        map.put("officeName", wordModel.getOfficeName());
        map.put("startDate", wordModel.getStartDate());
        map.put("idNumber", wordModel.getIdNumber());
        map.put("roleList", wordModel.getRoleList());
        map.put("address", wordModel.getAddress());
        map.put("phone", wordModel.getPhone());
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("educationList", policy)
                .bind("workList", policy)
                .bind("honoraryList", policy)
                .bind("softwareList", policy)
                .bind("projectList", policy)
                .bind("topicList", policy)
                .bind("academicPaperList", policy)
                .build();
        map.put("educationList", wordModel.getEducationExperienceList());
        map.put("workList", wordModel.getWorkExperienceList());
        map.put("honoraryList", wordModel.getHonoraryAwardList());
        map.put("softwareList", wordModel.getSoftwareList());
        map.put("projectList", wordModel.getProjectList());
        map.put("topicList", wordModel.getTopicList());
        map.put("academicPaperList", wordModel.getAcademicPaperList());
        //读取模板内容
        File templateFile = ResourceUtils.getFile(this.wordTemplateFile);
        XWPFTemplate compile = XWPFTemplate.compile(templateFile,config);
        //渲染数据到模板
        return compile.render(map);
    }

    //获取ExcelWriter excel写入器
    @Override
    public ExcelWriter getMyExcelWriter(List<Teacher> teacherList, String fileName, List<String> fieldList) {
        //获取数据
        List<TeacherExcelModel> teacherExcelModelList = teacherList.stream().map((teacher -> {
            Long tid = teacher.getId();
            Long cid = teacher.getCid();
            Long oid = teacher.getOid();
            TeacherExcelModel teacherExcelModel = new TeacherExcelModel(teacher);
            teacherExcelModel.setRoleList(this.getRoleListByTid(tid).toString().replaceAll("^\\[|\\]$", ""));//去掉首尾的中括号
            log.info(teacherExcelModel.getRoleList());
            teacherExcelModel.setCollegeName(collegeMapper.getCollegeNameByCid(cid));
            teacherExcelModel.setOfficeName(officeMapper.getOfficeNameByOid(oid));
/*            IdentityCardAudit identityCardAudit = this.queryIdentityCardShowByTid(tid);
            if (identityCardAudit != null) {
                teacherExcelModel.setIdentityCard(identityCardAudit.getIdNumber());
            }*/
            return teacherExcelModel;//返回的是TeacherExcelModel的集合
        })).collect(Collectors.toList());
        // 创建无敌ExcelWriter writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        int count = 0;
        if (fieldList != null && !fieldList.isEmpty()) {
            if (fieldList.contains("id")) {
                writer.addHeaderAlias("id", "教师id");
                count += 1;
            }
            if (fieldList.contains("teacherName")) {
                writer.addHeaderAlias("teacherName", "教师姓名");
                count += 1;
            }
            if (fieldList.contains("username")) {
                writer.addHeaderAlias("username", "登录账号");
                count += 1;
            }
            if (fieldList.contains("gender")) {
                writer.addHeaderAlias("gender", "性别");
                count += 1;
            }
            if (fieldList.contains("identityCard")) {
                writer.addHeaderAlias("identityCard", "身份证号");
                count += 1;
            }
            if (fieldList.contains("roleList")) {
                writer.addHeaderAlias("roleList", "职务");
                count += 1;
            }
            if (fieldList.contains("ethnic")) {
                writer.addHeaderAlias("ethnic", "民族");
                count += 1;
            }
            if (fieldList.contains("politicsStatus")) {
                writer.addHeaderAlias("politicsStatus", "政治面貌");
                count += 1;
            }
            if (fieldList.contains("nativePlace")) {
                writer.addHeaderAlias("nativePlace", "籍贯");
                count += 1;
            }
            if (fieldList.contains("address")) {
                writer.addHeaderAlias("address", "住址");
                count += 1;
            }
            if (fieldList.contains("phone")) {
                writer.addHeaderAlias("phone", "电话号码");
                count += 1;
            }

            if (fieldList.contains("collegeName")) {
                writer.addHeaderAlias("collegeName", "学院");
                count += 1;
            }
            if (fieldList.contains("officeName")) {
                writer.addHeaderAlias("officeName", "教研室");
                count += 1;
            }
            if (fieldList.contains("isAuditor")) {
                writer.addHeaderAlias("isAuditor", "审核员");
                count += 1;
            }
            if (fieldList.contains("startDate")) {
                writer.addHeaderAlias("startDate", "入校日期");
                count += 1;
            }
            log.error("count={}", count);
            log.error(fileName);
            writer.merge(count - 1, fileName);//合并标题
        } else {
            writer.addHeaderAlias("id", "教师id");
            writer.addHeaderAlias("teacherName", "教师姓名");
            writer.addHeaderAlias("username", "登录账号");
            writer.addHeaderAlias("gender", "性别");
            writer.addHeaderAlias("identityCard", "身份证号");
            writer.addHeaderAlias("roleList", "职务");
            writer.addHeaderAlias("ethnic", "民族");
            writer.addHeaderAlias("politicsStatus", "政治面貌");
            writer.addHeaderAlias("nativePlace", "籍贯");
            writer.addHeaderAlias("address", "住址");
            writer.addHeaderAlias("phone", "电话号码");
            writer.addHeaderAlias("collegeName", "学院");
            writer.addHeaderAlias("officeName", "教研室");
            writer.addHeaderAlias("isAuditor", "审核员");
            writer.addHeaderAlias("startDate", "入校日期");
            writer.merge(14, fileName);
        }
        log.info("teacherExcelList={}", teacherExcelModelList);
        //只写出加了别名的字段
        writer.setOnlyAlias(true);
        writer.write(teacherExcelModelList, true);//标题行true
//        writer.autoSizeColumnAll();//自动调整列宽
//        MyExcelUtil.setSizeColumn(writer.getSheet(),count-1);
        StyleSet style = writer.getStyleSet();
        Font font = writer.createFont();
        font.setColor(IndexedColors.VIOLET.index);
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        //重点，设置中文字体
        font.setFontName("宋体");
        style.getHeadCellStyle().setFont(font);
        int columnCount = writer.getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            //如果有合并列，最后一个参数设置为true
            double width = SheetUtil.getColumnWidth(writer.getSheet(), i, true);
            if (width != -1.0D) {
                width *= 256.0D;
                //此处可以适当调整，调整列空白处宽度
                width += 2200D;
                if (width > 255*256) {
                    width = 255*256;
                }
                writer.setColumnWidth(i, Math.toIntExact(Math.round(width / 256D)));
            }
        }
        return writer;
    }


    //获取excel文件
    @Override
    public File getMyExcelFile(List<Teacher> teacherList, List<String> fieldList) {
        ExcelWriter writer = this.getMyExcelWriter(teacherList, defaultExcelName, fieldList);
        File excelFile = new File(temporaryPath + sep + defaultExcelName + ".xlsx");
        writer.flush(excelFile);//直接将数据写入到文件中,并关闭ExcelWriter对象
        /*writer.flush(FileUtil.getOutputStream(excelFile), true);//true 关闭输出流*/
        writer.close();//双重保险
        return excelFile;
    }

    //获取word集合
    @Override
    public File getMyWordFolder(List<Teacher> teacherList) throws IOException {
        //创建临时word集合
        File tempWordFolder = new File(temporaryPath + sep + "word集合");
        FileUtil.mkdir(tempWordFolder);
        for (Teacher teacher : teacherList) {
            //word集合下新建以xx教师命名的word文件
            String tempTeacherWordPath = tempWordFolder + sep + teacher.getTeacherName() + ".docx";
            FileUtil.touch(tempTeacherWordPath);/////////if
            //获取渲染好数据的word
            XWPFTemplate render = this.getWordRenderByTid(teacher.getId());
            render.writeToFile(tempTeacherWordPath);
            render.close();//双重保险
        }
        return tempWordFolder;
    }

    //获取教师附件文件夹
    @Override
    public File getTeacherAttachmentFolder(List<Teacher> teacherList, List<String> attachmentList) {
        //创建临时教师附件文件夹
        File tempTeacherAttachmentFolder = new File(temporaryPath + sep + "教师附件");   //教师附件
        FileUtil.mkdir(tempTeacherAttachmentFolder);
        for (Teacher teacher : teacherList) {
            Long tid = teacher.getId();
            //教师附件文件夹下新建以xx教师命名的目录
            String tempTeacherPath = tempTeacherAttachmentFolder + sep + teacher.getTeacherName();//教师附件->教师名
            FileUtil.mkdir(tempTeacherPath);//if
            // ==================================== academicPaper ====================================
            if(true || attachmentList.contains("academicPaper")){
                LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(AcademicPaperAudit::getTid, tid)
                        .eq(AcademicPaperAudit::getAuditStatus, 1)
                        .eq(AcademicPaperAudit::getIsShow, 1);
                List<Long> ids = academicPaperService.list(queryWrapper).stream().map(AcademicPaperAudit::getId).collect(Collectors.toList());
                log.info("ids={}", ids);
                if (!ids.isEmpty()) {// 创建论文文件夹                B
                    String tempPath = tempTeacherPath + sep + "论文材料";//教师附件->教师名->论文材料
                    FileUtil.mkdir(tempPath);
                    // 遍历ids 依次找到对应的文件夹,并复制到临时文件夹中
                    for (Long id : ids) {
                        //找到该教师对应的文件夹       A
                        File findAcademicPaperFolder = new File(rootPath + sep + "academicPaper" + sep + tid + sep + id);
                        FileUtil.mkdir(findAcademicPaperFolder);//防止不存在报错
                        //copyContent(A,B)       A => B    复制A目录(下的文件) 到 B目录下,true表示覆盖
                        FileUtil.copyContent(findAcademicPaperFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== educationExperience ====================================
            if(true || attachmentList.contains("educationExperience")){
                LambdaQueryWrapper<EducationExperienceAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(EducationExperienceAudit::getTid, tid)
                        .eq(EducationExperienceAudit::getAuditStatus, 1)
                        .eq(EducationExperienceAudit::getIsShow, 1);
                List<Long> ids = educationExperienceService.list(queryWrapper).stream().map(EducationExperienceAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "教育经历";//教师附件->教师名->教育经历
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findEducationExperienceFolder = new File(rootPath + sep + "educationExperience" + sep + tid + sep + id);
                        FileUtil.mkdir(findEducationExperienceFolder);//防止不存在报错
                        FileUtil.copyContent(findEducationExperienceFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== honoraryAward ====================================
            if(true || attachmentList.contains("honoraryAward")){
                LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(HonoraryAwardAudit::getTid, tid)
                        .eq(HonoraryAwardAudit::getAuditStatus, 1)
                        .eq(HonoraryAwardAudit::getIsShow, 1);
                List<Long> ids = honoraryAwardService.list(queryWrapper).stream().map(HonoraryAwardAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "荣誉奖励";//教师附件->教师名->荣誉奖励
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findHonoraryAwardFolder = new File(rootPath + sep + "honoraryAward" + sep + tid + sep + id);
                        FileUtil.mkdir(findHonoraryAwardFolder);//防止不存在报错
                        FileUtil.copyContent(findHonoraryAwardFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== project ====================================
            if(true || attachmentList.contains("project")){
                LambdaQueryWrapper<ProjectAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ProjectAudit::getTid, tid)
                        .eq(ProjectAudit::getAuditStatus, 1)
                        .eq(ProjectAudit::getIsShow, 1);
                List<Long> ids = projectService.list(queryWrapper).stream().map(ProjectAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "项目";//教师附件->教师名->项目
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findProjectFolder = new File(rootPath + sep + "project" + sep + tid + sep + id);
                        FileUtil.mkdir(findProjectFolder);//防止不存在报错
                        FileUtil.copyContent(findProjectFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== software ====================================
            if(true || attachmentList.contains("software")){
                LambdaQueryWrapper<SoftwareAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SoftwareAudit::getTid, tid)
                        .eq(SoftwareAudit::getAuditStatus, 1)
                        .eq(SoftwareAudit::getIsShow, 1);
                List<Long> ids = softwareService.list(queryWrapper).stream().map(SoftwareAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "软件著作";//教师附件->教师名->软件著作
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findSoftwareFolder = new File(rootPath + sep + "software" + sep + tid + sep + id);
                        FileUtil.mkdir(findSoftwareFolder);//防止不存在报错
                        FileUtil.copyContent(findSoftwareFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== topic ====================================
            if(true || attachmentList.contains("topic")){
                LambdaQueryWrapper<TopicAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TopicAudit::getTid, tid)
                        .eq(TopicAudit::getAuditStatus, 1)
                        .eq(TopicAudit::getIsShow, 1);
                List<Long> ids = topicService.list(queryWrapper).stream().map(TopicAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "课题";//教师附件->教师名->课题
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findTopicFolder = new File(rootPath + sep + "topic" + sep + tid + sep + id);
                        FileUtil.mkdir(findTopicFolder);//防止不存在报错
                        FileUtil.copyContent(findTopicFolder, new File(tempPath), true);
                    }
                }
            }
            // ==================================== workExperience ====================================
            if(true || attachmentList.contains("workExperience")){
                LambdaQueryWrapper<WorkExperienceAudit> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(WorkExperienceAudit::getTid, tid)
                        .eq(WorkExperienceAudit::getAuditStatus, 1)
                        .eq(WorkExperienceAudit::getIsShow, 1);
                List<Long> ids = workExperienceService.list(queryWrapper).stream().map(WorkExperienceAudit::getId).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    String tempPath = tempTeacherPath + sep + "工作经历";//教师附件->教师名->工作经历
                    FileUtil.mkdir(tempPath);
                    for (Long id : ids) {
                        File findWorkExperienceFolder = new File(rootPath + sep + "workExperience" + sep + tid + sep + id);
                        FileUtil.mkdir(findWorkExperienceFolder);//防止不存在报错
                        FileUtil.copyContent(findWorkExperienceFolder, new File(tempPath), true);
                    }
                }
            }
            /*// ==================================== test ====================================
            *//*  test 测试第二类信息附件*//*
            File findTestFile = new File(rootPath + sep + "test" + sep + tid);
            FileUtil.mkdir(findTestFile);//防止不存在报错
            String tempTestPath = tempTeacherPath + sep + "测试目录2";
            FileUtil.mkdir(tempTestPath);
            FileUtil.copyContent(findTestFile, new File(tempTestPath), true);*/
        }
        return tempTeacherAttachmentFolder;
    }


    //========================根据tid获取教师正在显示的各种信息========================
    //论文
    private List<AcademicPaperVo> queryAcademicPaperShowListByTeacherId(Long tid){
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getTid,tid);  //根据tid查询
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getAuditStatus,1);//审核通过
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getIsShow,1);     //正在显示
        queryWrapper_academicPaper.orderByAsc(AcademicPaperAudit::getPublishDate);  //按发表时间升序
        return academicPaperService.list(queryWrapper_academicPaper).stream().map(academicPaperService::getDto).collect(Collectors.toList());
    }
    //教育经历
    private List<EducationExperienceVo> queryEducationExperienceShowListByTid(Long tid){
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper_educationExperience = new LambdaQueryWrapper<>();
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_educationExperience.orderByAsc(EducationExperienceAudit::getStaDate);  //按开始时间升序
        return educationExperienceService.list(queryWrapper_educationExperience).stream().map(educationExperienceService::getDto).collect(Collectors.toList());
    }
    //工作经历
    private List<WorkExperienceVo> queryWorkExperienceShowListByTid(Long tid){
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper_workExperience = new LambdaQueryWrapper<>();
        queryWrapper_workExperience.eq(WorkExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_workExperience.eq(WorkExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_workExperience.eq(WorkExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_workExperience.orderByAsc(WorkExperienceAudit::getStaDate);  //按开始时间升序
        return workExperienceService.list(queryWrapper_workExperience).stream().map(workExperienceService::getDto).collect(Collectors.toList());
    }
    //荣誉奖励
    private List<HonoraryAwardVo> queryHonoraryAwardShowListByTid(Long tid){
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper_honoraryAward = new LambdaQueryWrapper<>();
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getTid,tid);  //根据tid查询
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getAuditStatus,1);//审核通过
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getIsShow,1);     //正在显示
        queryWrapper_honoraryAward.orderByAsc(HonoraryAwardAudit::getGetDate);  //按获得时间升序
        return honoraryAwardService.list(queryWrapper_honoraryAward).stream().map(honoraryAwardService::getDto).collect(Collectors.toList());
    }
    //课题
    private List<TopicVo> queryTopicShowListByTeacherId(Long tid){
        LambdaQueryWrapper<TopicAudit> queryWrapper_topic = new LambdaQueryWrapper<>();
        queryWrapper_topic.eq(TopicAudit::getTid,tid);  //根据tid查询
        queryWrapper_topic.eq(TopicAudit::getAuditStatus,1);//审核通过
        queryWrapper_topic.eq(TopicAudit::getIsShow,1);     //正在显示
        queryWrapper_topic.orderByAsc(TopicAudit::getStaDate);  //按开始时间升序
        return topicService.list(queryWrapper_topic).stream().map(topicService::getDto).collect(Collectors.toList());
    }
    //项目
    private List<ProjectVo> queryProjectShowListByTeacherId(Long tid){
        LambdaQueryWrapper<ProjectAudit> queryWrapper_project = new LambdaQueryWrapper<>();
        queryWrapper_project.eq(ProjectAudit::getTid,tid);  //根据tid查询
        queryWrapper_project.eq(ProjectAudit::getAuditStatus,1);//审核通过
        queryWrapper_project.eq(ProjectAudit::getIsShow,1);     //正在显示
        queryWrapper_project.orderByAsc(ProjectAudit::getCompletionDate);  //按完成时间升序
        return projectService.list(queryWrapper_project).stream().map(projectService::getDto).collect(Collectors.toList());
    }
    //软件著作
    private List<SoftwareVo> querySoftwareShowListByTeacherId(Long tid){
        LambdaQueryWrapper<SoftwareAudit> queryWrapper_software = new LambdaQueryWrapper<>();
        queryWrapper_software.eq(SoftwareAudit::getTid,tid);  //根据tid查询
        queryWrapper_software.eq(SoftwareAudit::getAuditStatus,1);//审核通过
        queryWrapper_software.eq(SoftwareAudit::getIsShow,1);     //正在显示
        queryWrapper_software.orderByAsc(SoftwareAudit::getCompletionDate);  //按完成时间升序
        return softwareService.list(queryWrapper_software).stream().map(softwareService::getDto).collect(Collectors.toList());
    }
}
