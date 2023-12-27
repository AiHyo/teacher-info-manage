package com.aih.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.vo.*;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.*;
import com.aih.mapper.*;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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
            return data;
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    @Override//根据tid获取教师信息TeacherDto的
    public TeacherDetailDto queryTeacherDtoByTid(Long tid) {
        Teacher findTeacher = this.baseMapper.selectById(tid);
        findTeacher.setPassword(null);//将密码置空
        TeacherDetailDto teacherDetailDto = new TeacherDetailDto();
        BeanUtils.copyProperties(findTeacher, teacherDetailDto);//将findTeacher的属性复制到teacherDto

        //获取学院和科室名称
        teacherDetailDto.setCollegeName(collegeMapper.getCollegeNameByCid(findTeacher.getCid()));
        teacherDetailDto.setOfficeName(officeMapper.getOfficeNameByOid(findTeacher.getOid()));
        //获取职务
        List<String> roleNameByTeacherId = this.baseMapper.getRoleNameByTeacherId(tid);
        teacherDetailDto.setRoleList(String.join(",", roleNameByTeacherId));
/*        //获取正在显示的身份证资料
        teacherDetailDto.setIdentityCard(this.queryIdentityCardShowByTid(tid));*/
        //教育经历
        teacherDetailDto.setEducationExperienceList(this.queryEducationExperienceShowListByTid(tid));
        //工作经历
        teacherDetailDto.setWorkExperienceList(this.queryWorkExperienceShowListByTid(tid));
        //荣誉奖励
        teacherDetailDto.setHonoraryAwardList(this.queryHonoraryAwardShowListByTid(tid));
        //课题
        teacherDetailDto.setTopicList(this.queryTopicShowListByTeacherId(tid));
        //论文
        teacherDetailDto.setAcademicPaperList(this.queryAcademicPaperShowListByTeacherId(tid));
        //项目
        teacherDetailDto.setProjectList(this.queryProjectShowListByTeacherId(tid));
        //软件著作
        teacherDetailDto.setSoftwareList(this.querySoftwareShowListByTeacherId(tid));
        return teacherDetailDto;
    }

    @Override
    public void logout() {
        //退出登录,将token加入黑名单
    }

    @Override
    public Page<TeacherDto> getTeacherList(Integer pageNum, Integer pageSize, String teacherName, Integer gender, String ethnic, String birthplace, String address) {
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
        List<TeacherDto> collect = pageInfo.getRecords().stream().map((item) -> {
            TeacherDto teacherDto = new TeacherDto();
            BeanUtils.copyProperties(item, teacherDto);
            teacherDto.setCollegeName(collegeMapper.getCollegeNameByCid(item.getCid()));
            teacherDto.setOfficeName(officeMapper.getOfficeNameByOid(item.getOid()));
            teacherDto.setRoleList(String.join(",", this.baseMapper.getRoleNameByTeacherId(item.getId())));
            return teacherDto;
        }).collect(Collectors.toList());
        Page<TeacherDto> pageDtoInfo = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        pageDtoInfo.setRecords(collect);
        return pageDtoInfo;
    }

    @Override
    public Page<AuditInfoDto> getAuditList(Integer pageNum, Integer pageSize, Integer auditStatus, boolean onlyOwn) {
        Long uid = UserInfoContext.getUser().getId();
        Teacher cur_teacher = this.baseMapper.selectById(uid);
        //获取审核员有权限审核的tidList
        List<Long> auditorCanAuditTids = myUtil.getAuditorCanAuditTids();
        if(auditorCanAuditTids.isEmpty()){
            return null;
        }
        //封装所有的审核信息
        List<AuditInfoDto> all_auditInfoDtoList = new ArrayList<AuditInfoDto>();
        // === AcademicPaperAudit ===
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper
                .in(AcademicPaperAudit::getTid, auditorCanAuditTids)
                .eq(auditStatus!=null,AcademicPaperAudit::getAuditStatus, auditStatus)
                .ge(AcademicPaperAudit::getCreateTime, cur_teacher.getCreateDate())//上任以来(针对未审核的)
                .eq(onlyOwn,AcademicPaperAudit::getTid, cur_teacher.getId());//只看自己的(针对已审核的)
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
    public List<String> getRoleListByTid(Long id) {
        return this.baseMapper.getRoleNameByTeacherId(id);
    }

    @Override
    public XWPFTemplate getWordRender(Teacher teacher) throws IOException {
        //获取模板
        File templateFile = ResourceUtils.getFile(this.wordTemplateFile);
        //准备数据
        Map<String, Object> map = new HashMap<>();
        map.put("userName", teacher.getUsername());
        map.put("teacherName",teacher.getTeacherName());
        map.put("createDate",teacher.getCreateDate());
        List<String> arrays = this.getRoleListByTid(teacher.getId());
        map.put("arrays", String.join("\n", arrays));
        //读取模板内容
        XWPFTemplate compile = XWPFTemplate.compile(templateFile);
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
        if (fieldList != null && !fieldList.isEmpty()) {
            int count = 0;
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
            if (fieldList.contains("birthplace")) {
                writer.addHeaderAlias("birthplace", "籍贯");
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
            writer.addHeaderAlias("birthplace", "籍贯");
            writer.addHeaderAlias("address", "住址");
            writer.addHeaderAlias("phone", "电话号码");
            writer.addHeaderAlias("collegeName", "学院");
            writer.addHeaderAlias("officeName", "教研室");
            writer.addHeaderAlias("isAuditor", "审核员");
            writer.addHeaderAlias("startDate", "入校日期");
            writer.merge(13, fileName);
        }
        log.info("teacherExcelList={}", teacherExcelModelList);
        //只写出加了别名的字段
        writer.setOnlyAlias(true);
        writer.write(teacherExcelModelList, true);//标题行true
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
            XWPFTemplate render = this.getWordRender(teacher);
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

            log.info("attachmentList={}", attachmentList);//=== iter ===这里完善后需要依据attachmentList
            // ==================================== academicPaper ====================================
            LambdaQueryWrapper<AcademicPaperAudit> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AcademicPaperAudit::getTid, tid)
                    .eq(AcademicPaperAudit::getAuditStatus, 1)
                    .eq(AcademicPaperAudit::getIsShow, 1);
            List<Long> ids = academicPaperService.list(queryWrapper).stream().map(AcademicPaperAudit::getId).collect(Collectors.toList());
            // 创建论文文件夹                B
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
            // ==================================== test ====================================
            /*  test 测试第二类信息附件*/
            File findTestFile = new File(rootPath + sep + "test" + sep + tid);
            FileUtil.mkdir(findTestFile);//防止不存在报错
            String tempTestPath = tempTeacherPath + sep + "测试目录2";
            FileUtil.mkdir(tempTestPath);
            FileUtil.copyContent(findTestFile, new File(tempTestPath), true);
        }
        return tempTeacherAttachmentFolder;
    }


    //========================根据tid获取教师正在显示的各种信息========================
    //论文
    private List<AcademicPaperDto> queryAcademicPaperShowListByTeacherId(Long tid){
        LambdaQueryWrapper<AcademicPaperAudit> queryWrapper_academicPaper = new LambdaQueryWrapper<>();
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getTid,tid);  //根据tid查询
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getAuditStatus,1);//审核通过
        queryWrapper_academicPaper.eq(AcademicPaperAudit::getIsShow,1);     //正在显示
        queryWrapper_academicPaper.orderByAsc(AcademicPaperAudit::getPublishDate);  //按发表时间升序
        List<Long> ids = academicPaperService.list(queryWrapper_academicPaper).stream().map(AcademicPaperAudit::getId).collect(Collectors.toList());
        List<AcademicPaperDto> collect = ids.stream().map((id) -> {
            AcademicPaperDto dto = academicPaperService.queryDtoById(id);
            return dto;
        }).collect(Collectors.toList());
        return collect;
    }
    //身份证材料
    public IdentityCardAudit queryIdentityCardShowByTid(Long tid){
        LambdaQueryWrapper<IdentityCardAudit> queryWrapper_identityCard = new LambdaQueryWrapper<>();
        queryWrapper_identityCard.eq(IdentityCardAudit::getTid,tid);  //根据tid查询
        queryWrapper_identityCard.eq(IdentityCardAudit::getAuditStatus,1);//审核通过
        queryWrapper_identityCard.eq(IdentityCardAudit::getIsShow,1);     //正在显示
        return identityCardAuditMapper.selectOne(queryWrapper_identityCard);
    }
    //教育经历
    private List<EducationExperienceAudit> queryEducationExperienceShowListByTid(Long tid){
        LambdaQueryWrapper<EducationExperienceAudit> queryWrapper_educationExperience = new LambdaQueryWrapper<>();
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_educationExperience.eq(EducationExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_educationExperience.orderByAsc(EducationExperienceAudit::getStaDate);  //按开始时间升序
        return educationExperienceService.list(queryWrapper_educationExperience);
    }
    //工作经历
    private List<WorkExperienceAudit> queryWorkExperienceShowListByTid(Long tid){
        LambdaQueryWrapper<WorkExperienceAudit> queryWrapper_workExperience = new LambdaQueryWrapper<>();
        queryWrapper_workExperience.eq(WorkExperienceAudit::getTid,tid);  //根据tid查询
        queryWrapper_workExperience.eq(WorkExperienceAudit::getAuditStatus,1);//审核通过
        queryWrapper_workExperience.eq(WorkExperienceAudit::getIsShow,1);     //正在显示
        queryWrapper_workExperience.orderByAsc(WorkExperienceAudit::getStaDate);  //按开始时间升序
        return workExperienceService.list(queryWrapper_workExperience);
    }
    //荣誉奖励
    private List<HonoraryAwardAudit> queryHonoraryAwardShowListByTid(Long tid){
        LambdaQueryWrapper<HonoraryAwardAudit> queryWrapper_honoraryAward = new LambdaQueryWrapper<>();
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getTid,tid);  //根据tid查询
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getAuditStatus,1);//审核通过
        queryWrapper_honoraryAward.eq(HonoraryAwardAudit::getIsShow,1);     //正在显示
        queryWrapper_honoraryAward.orderByAsc(HonoraryAwardAudit::getGetDate);  //按获得时间升序
        return honoraryAwardService.list(queryWrapper_honoraryAward);
    }
    //课题
    private List<TopicAudit> queryTopicShowListByTeacherId(Long tid){
        LambdaQueryWrapper<TopicAudit> queryWrapper_topic = new LambdaQueryWrapper<>();
        queryWrapper_topic.eq(TopicAudit::getTid,tid);  //根据tid查询
        queryWrapper_topic.eq(TopicAudit::getAuditStatus,1);//审核通过
        queryWrapper_topic.eq(TopicAudit::getIsShow,1);     //正在显示
        queryWrapper_topic.orderByAsc(TopicAudit::getStaDate);  //按开始时间升序
        return topicService.list(queryWrapper_topic);
    }
    //项目
    private List<ProjectAudit> queryProjectShowListByTeacherId(Long tid){
        LambdaQueryWrapper<ProjectAudit> queryWrapper_project = new LambdaQueryWrapper<>();
        queryWrapper_project.eq(ProjectAudit::getTid,tid);  //根据tid查询
        queryWrapper_project.eq(ProjectAudit::getAuditStatus,1);//审核通过
        queryWrapper_project.eq(ProjectAudit::getIsShow,1);     //正在显示
        queryWrapper_project.orderByAsc(ProjectAudit::getCompletionDate);  //按完成时间升序
        return projectService.list(queryWrapper_project);
    }
    //软件著作
    private List<SoftwareAudit> querySoftwareShowListByTeacherId(Long tid){
        LambdaQueryWrapper<SoftwareAudit> queryWrapper_software = new LambdaQueryWrapper<>();
        queryWrapper_software.eq(SoftwareAudit::getTid,tid);  //根据tid查询
        queryWrapper_software.eq(SoftwareAudit::getAuditStatus,1);//审核通过
        queryWrapper_software.eq(SoftwareAudit::getIsShow,1);     //正在显示
        queryWrapper_software.orderByAsc(SoftwareAudit::getCompletionDate);  //按完成时间升序
        return softwareService.list(queryWrapper_software);
    }
}
