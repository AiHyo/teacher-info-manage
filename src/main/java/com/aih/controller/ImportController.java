package com.aih.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Teacher;
import com.aih.entity.vo.export.excel.TeacherExcelReaderByAdmin;
import com.aih.entity.vo.export.excel.TeacherExcelReaderBySuperAdmin;
import com.aih.service.ICollegeService;
import com.aih.service.IOfficeService;
import com.aih.service.ITeacherService;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.utils.vo.RoleType;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/import")
public class ImportController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ICollegeService collegeService;
    @Autowired
    private IOfficeService officeService;
    @Autowired
    private ITeacherService teacherService;
    @Value("${default-password}")
    private String defaultPassword;

    @Value("${file.template-file-excel-superadmin}")
    String excelTemplateFileSuperAdmin;
    @Value("${file.template-file-excel-admin}")
    String excelTemplateFileAdmin;
    @Value("${excel.import-template-name}")
    String importTemplateName;
    //下载导入教师账号模板文件

    /**
     * [超管/管理员]下载导入教师账号模板文件 如果文件不存在,返回自定义信息。
     */
    @ApiOperation("下载导入所需文件模板")
    @GetMapping("/downloadExcelTemplate")
    public R<?> downloadExcelTemplate(HttpServletResponse response) throws IOException {
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if (roleType != RoleType.SUPER_ADMIN && roleType != RoleType.ADMIN){
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        File file = null;
        InputStream inputStream = null;
        log.info(excelTemplateFileAdmin+"=================");
        if (roleType == RoleType.SUPER_ADMIN) {
//            ClassPathResource sourceFile = new ClassPathResource(excelTemplateFileSuperAdmin);
//            file = sourceFile.getFile();
//            file = ResourceUtils.getFile(excelTemplateFileSuperAdmin);
            ClassPathResource classPathResource = new ClassPathResource(excelTemplateFileSuperAdmin);
            inputStream = classPathResource.getInputStream();
        }else {
            ClassPathResource classPathResource = new ClassPathResource(excelTemplateFileAdmin);
            inputStream = classPathResource.getInputStream();
        }
        if (file == null&&inputStream==null){
            throw new CustomException(CustomExceptionCodeMsg.TEMPLATE_FILE_NOT_EXIST);
        }
        //根据inputStream,下载文件
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(importTemplateName, "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
//        //对文件名编码,解决中文文件名乱码问题
//        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(importTemplateName, "UTF-8") + ".xlsx");  // 下载
//        byte[] bytes = FileUtil.readBytes(file);
//        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.write(bytes);
//        outputStream.flush();
//        outputStream.close();
        return R.success("下载成功");
    }

    /**
     * [超管/管理员]Excel批量导入教师信息.超管若不填写办公室名称,则默认id0的办公室,若手机号已存在,若学院/办公室不存在,返回对应不存在的信息.管理员填写学院无效。
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation("Excel批量导入教师信息")
    @PostMapping("/excel")
    @Transactional(rollbackFor=Exception.class) //事务回滚
    public R<?> importExcel(MultipartFile file) throws IOException {
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if (roleType != RoleType.SUPER_ADMIN && roleType != RoleType.ADMIN){
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        InputStream inputStream = file.getInputStream();//字节输入流
        ExcelReader reader = ExcelUtil.getReader(inputStream);//通过输入流创建ExcelReader 对象
        if (roleType==RoleType.SUPER_ADMIN) {
            reader.addHeaderAlias("学院名称", "collegeName");
        }
        reader.addHeaderAlias("手机号", "phone");
        reader.addHeaderAlias("办公室名称", "officeName");
        reader.addHeaderAlias("工位号","deskId");
        reader.addHeaderAlias("教师姓名","teacherName");
        // 准备
        List<Teacher> teacherList = null;
        Map<String,Integer> college_map = new HashMap<>();
        Map<String,Integer> office_map = new HashMap<>();
        // 读取
        if (roleType==RoleType.SUPER_ADMIN) {
            List<TeacherExcelReaderBySuperAdmin> readerList = reader.readAll(TeacherExcelReaderBySuperAdmin.class);
            // 处理成需要插入的Teacher数据
            teacherList = readerList.stream().map(obj -> {
                //判空
                String phone = obj.getPhone();
                Long deskId = obj.getDesk_id();
                String teacherName = obj.getTeacherName();
                String collegeName1 = obj.getCollegeName();
                if (StrUtil.isBlank(phone)){
                    throw new CustomException(1090,"手机号不能为空");
                }
                if (deskId == null){
                    throw new CustomException(1093,"工位号不能为空");
                }
                if (StrUtil.isBlank(teacherName)){
                    throw new CustomException(1094,"教师姓名不能为空");
                }
                if (StrUtil.isBlank(collegeName1)){
                    throw new CustomException(1091,"学院名称不能为空");
                }
                //判断重复
                if (teacherService.checkUsernameExist(phone)) {
                    throw new CustomException(1090,"手机号"+phone+"已经存在");
                }
                if(teacherService.checkDeskIdExist(deskId)){
                    throw new CustomException(1093,"工位号"+deskId+"已经存在");
                }
                //生成对象
                Teacher teacher = new Teacher();
                teacher.setUsername(phone); //登录账号默认手机号
                teacher.setPassword(passwordEncoder.encode(defaultPassword));
                teacher.setIsAuditor(0); //插入的数据都默认0
                teacher.setPhone(obj.getPhone());//手机号
                teacher.setDeskId(deskId); // 工位号
                teacher.setTeacherName(teacherName);//教师姓名
                String collegeName = obj.getCollegeName();
                if (StrUtil.isBlank(collegeName)){
                    throw new CustomException(1091,"手机号"+phone+"的学院名称不能为空");
                }
                Long cid = collegeService.getCidByName(collegeName);
                if (cid == null){
                    throw new CustomException(1092,"学院名称"+collegeName+"不存在");
                }
                teacher.setCid(cid);
                String officeName = obj.getOfficeName();
                if (StrUtil.isBlank(officeName)){ //空就默认没隶属办公室
                    officeName = officeService.getOfficeNameByOid(0L);
                }
                Long oid = officeService.getOidByNameAndCid(officeName, cid);
                if (oid == null){
                    throw new CustomException(1093,"办公室名称"+officeName+"不存在");
                }
                teacher.setOid(oid);
                //计数
                college_map.put(collegeName,college_map.getOrDefault(collegeName,0)+1);
                office_map.put(officeName,office_map.getOrDefault(officeName,0)+1);
                return teacher;
            }).collect(Collectors.toList());
        }
        else { //管理员插入的教师数据都是自己学院的
            Long cid = UserInfoContext.getUser().getCid();
            if (cid == 0){
                throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
            }
            String collegeName = collegeService.getCollegeNameByCid(cid);
            List<TeacherExcelReaderByAdmin> readerList = reader.readAll(TeacherExcelReaderByAdmin.class);
            teacherList = readerList.stream().map(obj -> {
                String phone = obj.getPhone();
                Long deskId = obj.getDeskId();
                String teacherName = obj.getTeacherName();
                String officeName1 = obj.getOfficeName();
                //判空
                if (StrUtil.isBlank(phone)){
                    throw new CustomException(1090,"手机号不能为空");
                }
                if (deskId == null){
                    throw new CustomException(1093,"工位号不能为空");
                }
                if (StrUtil.isBlank(teacherName)){
                    throw new CustomException(1094,"教师姓名不能为空");
                }
                if (StrUtil.isBlank(officeName1)){
                    throw new CustomException(1092,"办公室名称不能为空");
                }
                //判断重复
                if (teacherService.checkUsernameExist(phone)) {
                    throw new CustomException(1090,"手机号"+phone+"已经存在");
                }
                if(teacherService.checkDeskIdExist(deskId)){
                    throw new CustomException(1093,"工位号"+deskId+"已经存在");
                }
                //生成对象
                Teacher teacher = new Teacher();
                teacher.setUsername(phone); //登录账号默认手机号
                teacher.setPassword(passwordEncoder.encode(defaultPassword));
                teacher.setIsAuditor(0); //插入的数据都默认0
                teacher.setPhone(obj.getPhone()); //手机号
                teacher.setDeskId(deskId); //工位号
                teacher.setTeacherName(teacherName); //教师姓名
                teacher.setCid(collegeService.getCidByName(collegeName)); //学院id
                Long oid = officeService.getOidByNameAndCid(officeName1, cid);
                if (oid == null){
                    throw new CustomException(1093,"办公室名称"+officeName1+"不存在");
                }
                Long findCid = officeService.getCidById(oid);
                if (!Objects.equals(findCid, cid)){
                    throw new CustomException(1092,"办公室名称"+officeName1+"不属于"+collegeName);
                }
                teacher.setOid(oid); //办公室id
                //计数
                college_map.put(collegeName,college_map.getOrDefault(collegeName,0)+1);
                office_map.put(officeName1,office_map.getOrDefault(officeName1,0)+1);
                return teacher;
            }).collect(Collectors.toList());
        }
        try {
            teacherService.saveBatch(teacherList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CustomExceptionCodeMsg.EXCEL_IMPORT_ERROR);
        }
//        List<Map<String, Integer>> res = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        res.put("college",college_map);
        res.put("office",office_map);
        return R.success(res);
    }
}
