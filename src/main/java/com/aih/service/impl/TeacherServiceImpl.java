package com.aih.service.impl;

import com.aih.common.R;
import com.aih.common.utils.JwtUtil;
import com.aih.entity.*;
import com.aih.entity.dto.TeacherDto;
import com.aih.exception.CustomException;
import com.aih.exception.CustomExceptionCodeMsg;
import com.aih.mapper.*;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.management.relation.RoleList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Resource
    private IdentityCardAuditMapper identityCardAuditMapper;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private OfficeMapper officeMapper;

    private RoleMapper roleMapper;
    @Autowired
    private EducationExperienceAuditServiceImpl educationExperienceService;
    @Autowired
    private WorkExperienceAuditServiceImpl workExperienceService;
    @Autowired
    private HonoraryAwardAuditServiceImpl honoraryAwardService;
    @Autowired
    private TopicAuditServiceImpl topicService;
    @Autowired
    private AcademicPaperAuditServiceImpl academicPaperService;
    @Autowired
    private ProjectAuditServiceImpl projectService;
    @Autowired
    private SoftwareAuditServiceImpl softwareService;

    @Override
    public Map<String, Object> login(Teacher teacher) {
        //根据教师用户名查询
        log.info("teacher:{}",teacher);
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getUsername,teacher.getUsername());
        Teacher loginTeacher = this.baseMapper.selectOne(queryWrapper);
        //如果不为空,并且密码和传入密码相匹配,则生成token
        log.info("loginTeacher:{}",loginTeacher);
        if(loginTeacher!=null && passwordEncoder.matches(teacher.getPwd(),loginTeacher.getPwd()))
        {
            loginTeacher.setPwd(null);
            //创建jwt
            String token = jwtUtil.createToken(loginTeacher);
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }

    @Override
    public TeacherDto getTeacherInfoByToken(String token) {
        Teacher loginTeacher = null;
        //解析token
        try {
            loginTeacher = jwtUtil.parseToken(token, Teacher.class);
        } catch (Exception e) {
            log.error("token解析失败");
        }
        if (loginTeacher == null){
            return null;
        }
        Long tid = loginTeacher.getId();
        TeacherDto teacherDto = new TeacherDto();
        BeanUtils.copyProperties(loginTeacher,teacherDto);//将loginTeacher的属性复制到teacherDto、

        //获取学院和科室名称
        teacherDto.setCollegeName(collegeMapper.getCollegeNameById(loginTeacher.getCid()));
        teacherDto.setOfficeName(officeMapper.getOfficeNameById(loginTeacher.getOid()));
        //获取职务
        teacherDto.setRoleList(this.baseMapper.getRoleNameByTeacherId(tid));

        //获取正在显示的身份证资料
        teacherDto.setIdentityCard(this.queryIdentityCardShowByTeacherId(tid));
        //教育经历
        teacherDto.setEducationExperienceList(this.queryEducationExperienceShowListByTeacherId(tid));
        //工作经历
        teacherDto.setWorkExperienceList(this.queryWorkExperienceShowListByTeacherId(tid));
        //荣誉奖励
        teacherDto.setHonoraryAwardList(this.queryHonoraryAwardShowListByTeacherId(tid));
        //课题
        teacherDto.setTopicList(this.queryTopicShowListByTeacherId(tid));
        //论文
        teacherDto.setAcademicPaperList(this.queryAcademicPaperShowListByTeacherId(tid));
        //项目
        teacherDto.setProjectList(this.queryProjectShowListByTeacherId(tid));
        //软件著作
        teacherDto.setSoftwareList(this.querySoftwareShowListByTeacherId(tid));

        return teacherDto;
    }

    @Override
    public void logout(String token) {
        //退出登录,将token加入黑名单
    }

    //封装一个根据tid获取正在显示的身份证资料的方法
    private IdentityCardAudit queryIdentityCardShowByTeacherId(Long tid){
        LambdaQueryWrapper<IdentityCardAudit> queryWrapper_identityCard = new LambdaQueryWrapper<>();
        queryWrapper_identityCard.eq(IdentityCardAudit::getTid,tid);  //根据tid查询
        queryWrapper_identityCard.eq(IdentityCardAudit::getAuditStatus,1);//审核通过
        queryWrapper_identityCard.eq(IdentityCardAudit::getIsShow,1);     //正在显示
        IdentityCardAudit identityCard = identityCardAuditMapper.selectOne(queryWrapper_identityCard);
        return identityCard;
    }
    //教育经历
    private List<EducationExperienceAudit> queryEducationExperienceShowListByTeacherId(Long tid){
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper_educationExperience = new LambdaQueryWrapper<>();
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_educationExperience.orderByAsc(EducationExperienceAudit::getStaDate);  //按开始时间升序
        List<EducationExperienceAudit> educationExperienceList = educationExperienceService.list(queryWrapper_educationExperience);
        return educationExperienceList;
    }
    //工作经历
    private List<WorkExperienceAudit> queryWorkExperienceShowListByTeacherId(Long tid){
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper_workExperience = new LambdaQueryWrapper<>();
        queryWrapper_workExperience.eq(WorkExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_workExperience.eq(WorkExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_workExperience.eq(WorkExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_workExperience.orderByAsc(WorkExperienceAudit::getStaDate);  //按开始时间升序
        List<WorkExperienceAudit> workExperienceList = workExperienceService.list(queryWrapper_workExperience);
        return workExperienceList;
    }
    //荣誉奖励
    private List<HonoraryAwardAudit> queryHonoraryAwardShowListByTeacherId(Long tid){
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper_honoraryAward = new LambdaQueryWrapper<>();
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getTid,tid);  //根据tid查询
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getAuditStatus,1);//审核通过
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getIsShow,1);     //正在显示
        queryWrapper_honoraryAward.orderByAsc(HonoraryAwardAudit::getGetDate);  //按获得时间升序
        List<HonoraryAwardAudit> honoraryAwardList = honoraryAwardService.list(queryWrapper_honoraryAward);
        return honoraryAwardList;
    }
    //课题
    private List<TopicAudit> queryTopicShowListByTeacherId(Long tid){
        LambdaQueryWrapper<TopicAudit> queryWrapper_topic = new LambdaQueryWrapper<>();
        queryWrapper_topic.eq(TopicAudit::getTid,tid);  //根据tid查询
        queryWrapper_topic.eq(TopicAudit::getAuditStatus,1);//审核通过
        queryWrapper_topic.eq(TopicAudit::getIsShow,1);     //正在显示
        queryWrapper_topic.orderByAsc(TopicAudit::getStaDate);  //按开始时间升序
        List<TopicAudit> topicList = topicService.list(queryWrapper_topic);
        return topicList;
    }
    //论文
    private List<AcademicPaperAudit> queryAcademicPaperShowListByTeacherId(Long tid){
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getTid,tid);  //根据tid查询
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getAuditStatus,1);//审核通过
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getIsShow,1);     //正在显示
        queryWrapper_academicPaper.orderByAsc(AcademicPaperAudit::getPublishDate);  //按发表时间升序
        List<AcademicPaperAudit> academicPaperList = academicPaperService.list(queryWrapper_academicPaper);
        return academicPaperList;
    }
    //项目
    private List<ProjectAudit> queryProjectShowListByTeacherId(Long tid){
        LambdaQueryWrapper<ProjectAudit> queryWrapper_project = new LambdaQueryWrapper<>();
        queryWrapper_project.eq(ProjectAudit::getTid,tid);  //根据tid查询
        queryWrapper_project.eq(ProjectAudit::getAuditStatus,1);//审核通过
        queryWrapper_project.eq(ProjectAudit::getIsShow,1);     //正在显示
        queryWrapper_project.orderByAsc(ProjectAudit::getCompletionDate);  //按完成时间升序
        List<ProjectAudit> projectList = projectService.list(queryWrapper_project);
        return projectList;
    }
    //软件著作
    private List<SoftwareAudit> querySoftwareShowListByTeacherId(Long tid){
        LambdaQueryWrapper<SoftwareAudit> queryWrapper_software = new LambdaQueryWrapper<>();
        queryWrapper_software.eq(SoftwareAudit::getTid,tid);  //根据tid查询
        queryWrapper_software.eq(SoftwareAudit::getAuditStatus,1);//审核通过
        queryWrapper_software.eq(SoftwareAudit::getIsShow,1);     //正在显示
        queryWrapper_software.orderByAsc(SoftwareAudit::getCompletionDate);  //按完成时间升序
        List<SoftwareAudit> softwareList = softwareService.list(queryWrapper_software);
        return softwareList;
    }
}
