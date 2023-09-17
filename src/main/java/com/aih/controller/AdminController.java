package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.entity.*;
import com.aih.entity.vo.TeacherDto;
import com.aih.entity.vo.TeacherExcelModel;
import com.aih.mapper.CollegeMapper;
import com.aih.mapper.OfficeMapper;
import com.aih.service.IAdminService;
import com.aih.service.ITeacherService;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private CollegeMapper collegeMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Resource //@Autowired
    private HttpServletResponse response;

    /**
     * 登录成功返回token
     */
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Admin admin) {
        Map<String, Object> data = adminService.login(admin);
        return R.success(data);
    }

    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public R<?> logout() {
        adminService.logout();
        return R.success("登出成功");
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/showInfo")
    public R<Admin> showInfo() {
        Admin admin = adminService.showInfo();
        return R.success(admin);
    }


    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return R.success("修改成功");
    }

    /**
     * 路径参数 0:设为非审核员  1:设为审核员  传入ids/token非法 抛出自定义异常
     */
    @ApiOperation("批量修改审核员权限")
    @PostMapping("/auditorPower/{isAuditor}")
    public R<String> auditorPower(@PathVariable Integer isAuditor, @RequestParam List<Long> ids) {
        //判断ids是否合法
        LambdaQueryWrapper<Teacher> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.in(Teacher::getId, ids);
        long count = teacherService.count(queryWrapper_1);
        if (count != ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Teacher::getId, ids);
        List<Teacher> teachers = teacherService.list(queryWrapper);
        for (Teacher teacher : teachers) {
            if (!teacher.getCid().equals(cid)) {
                throw new CustomException(CustomExceptionCodeMsg.UPDATE_AUDIT_POWER_ERROR);
            }
        }
        adminService.updateIsAuditor(isAuditor, ids);
        return R.success("更改审核员权限成功");
    }

    /**
     * @param officeName 可选 模糊查询教研室名称
     */
    @ApiOperation("查询学院下的教研室")
    @GetMapping("/getOfficeList")
    public R<List<Office>> getOfficeList(@RequestParam(value = "officeName", required = false) String officeName) {
        Long cid = UserInfoContext.getUser().getCid();
        List<Office> officeList = officeMapper.getOfficeList(cid, officeName);
        return R.success(officeList);
    }

    /**
     * 可选模糊查询 keyword: 教研室/教师名称
     */
    @ApiOperation("查询学院下的教师")
    @GetMapping("/getTeacherList")
    public R<Page<Teacher>> getTeacherList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam(value = "isAuditor", required = false) Integer isAuditor,
                                           @RequestParam(value = "gender", required = false) Integer gender,
                                           @RequestParam(value = "ethnic", required = false) String ethnic,
                                           @RequestParam(value = "birthplace", required = false) String birthplace,
                                           @RequestParam(value = "address", required = false) String address) {
        Page<Teacher> pageInfo = new Page<>(pageNum, pageSize);
        return R.success(adminService.getTeacherList(pageInfo, keyword, isAuditor, gender, ethnic, birthplace, address));
    }


    @ApiOperation("查看教师详细信息")
    @GetMapping("/getTeacherInfo/{tid}")
    public R<TeacherDto> getTeacherInfo(@PathVariable Long tid) {
        //判断权限
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        if (!findTeacher.getCid().equals(UserInfoContext.getUser().getCid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        return R.success(teacherService.queryTeacherDtoByTid(tid));
    }


    /////////////////////////////////////////////excel/////////////////////////////////////////

    /**
     * @param ids       （可选）
     * @param oids      （可选）教研室oids
     * @param fileName  （可选）导出excel的名称,默认"教师信息表"
     * @param fieldList 只导出部分字段（可选）id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
     * @throws IOException
     */
    @ApiOperation("Excel批量导出教师信息")
    @GetMapping("/export")
    public R<?> exportExcel(@RequestParam(value = "ids", required = false) List<Long> ids,
                            @RequestParam(value = "oids", required = false) List<Long> oids,
                            @RequestParam(value = "fileName", required = false) String fileName,
                            @RequestParam(value = "fieldList", required = false) List<String> fieldList) throws IOException {
        ExcelWriter writer = this.getExcelWriterByParams(ids, oids, fileName, fieldList);
        //设置response并写出xlsx
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);//true 关闭输出流
        writer.close();
        outputStream.flush();
        outputStream.close();
        log.info("导出成功");
        return R.success("导出成功");
    }


    @Value("${ip:localhost}")
    String ip;
    @Value("${server.port}")
    String port;
    @Value("${file.root-path}")
    String rootPath;
    private final String sep = File.separator;

    String temporaryPath;
    @PostConstruct
    public void init() {
        temporaryPath = rootPath + sep + "temporary";
    }
    //利用hutool导出压缩包

    @ApiOperation("导出压缩包")
    @GetMapping("/exportZip/")
    public void exportZip(@RequestParam(value = "ids", required = false) List<Long> ids,
                          @RequestParam(value = "oids", required = false) List<Long> oids,
                          @RequestParam(value = "fieldList", required = false) List<String> fieldList,
                          @RequestParam(value = "attachmentList", required = false) List<String> attachmentList) throws IOException {
        String sep = File.separator;
        //创建临时zip包
        File tempZipFile = new File(temporaryPath + sep + "test压缩.zip");
        //需要压缩的文件列表
        List<File> fileList = CollUtil.newArrayList();
        //调用封装函数获取教师列表
        List<Teacher> teacherList = this.getTeacherListByParams(UserInfoContext.getUser().getCid(), ids, oids);
        //教师附件文件夹
        File tempTeacherListFile = new File(temporaryPath + sep + "教师附件");
        FileUtil.mkdir(tempTeacherListFile);
        for (Teacher teacher : teacherList) {
            Long tid = teacher.getId();
            Teacher findTeacher = teacherService.getById(tid);
            //教师附件文件夹下新建以xx教师姓名命名的目录
            String tempTeacherPath = tempTeacherListFile + sep + findTeacher.getTeacherName();
            FileUtil.mkdir(tempTeacherPath);

            //从云端找到该教师对应的文件
            File findAcademicPaperFile = new File(rootPath + sep + "academicPaper" + sep + tid);
            FileUtil.mkdir(findAcademicPaperFile);//防止不存在报错
            //创建对应文件夹
            String tempAcademicPaperPath = tempTeacherPath + sep + "论文材料";//
            FileUtil.mkdir(tempAcademicPaperPath);
            //copyContent(A,B) 复制A目录下的文件 到 B目录下,true表示覆盖
            FileUtil.copyContent(findAcademicPaperFile, new File(tempAcademicPaperPath), true);

            /*  test 测试第二类信息附件*/
            File findTestFile = new File(rootPath + sep + "test" + sep + tid);
            FileUtil.mkdir(findTestFile);//防止不存在报错
            String tempTestPath = tempTeacherPath + sep + "测试目录2";
            FileUtil.mkdir(tempTestPath);
            FileUtil.copyContent(findTestFile, new File(tempTestPath), true);
        }


        fileList.add(tempTeacherListFile);
        fileList.add(this.getTeachersExcelFileByParams(ids, oids, fieldList));
        fileList.add(new File("D:" + sep + "qwq.jpg"));
        //压缩多个文件
        ZipUtil.zip(tempZipFile, true, fileList.toArray(new File[fileList.size()]));
        //下载zip到浏览器！！！
        MyUtil.downloadZip(tempZipFile, response);
        //删除临时路径 包括所有临时文件夹及文件
        FileUtil.del(temporaryPath);//删除临时zip包
    }


    private List<Teacher> getTeacherListByParams(Long u_cid, List<Long> ids, List<Long> oids) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        /*权限只有自己管理学院*/
        queryWrapper.eq(Teacher::getCid, UserInfoContext.getUser().getCid());
        //根据传参判断选择的教师
        if (oids != null && !oids.isEmpty()) {//优先看oids
            for (Long oid : oids) {
                Office office = officeMapper.selectById(oid);
                if (office == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_OFFICE);
                }
                if (!office.getCid().equals(u_cid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法oid就根据oids选
            queryWrapper.in(Teacher::getOid, oids);
        } else if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
                }
                if (!teacher.getCid().equals(u_cid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法id就根据ids选
            queryWrapper.in(Teacher::getId, ids);
        } else log.info("没有选择需要打印的教师,准备导出管理学院下所有教师信息");
        return teacherService.list(queryWrapper);
    }


    private ExcelWriter getExcelWriterByParams(List<Long> ids, List<Long> oids, String fileName, List<String> fieldList) {
        if (StrUtil.isBlank(fileName)) {
            fileName = "教师信息表";
        }
        Long u_cid = UserInfoContext.getUser().getCid();
        // 创建无敌ExcelWriter writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //获取数据
        List<Teacher> teacherList = this.getTeacherListByParams(u_cid, ids, oids);
        List<TeacherExcelModel> teacherExcelModelList = teacherList.stream().map((teacher -> {
            Long tid = teacher.getId();
            Long cid = teacher.getCid();
            Long oid = teacher.getOid();
            TeacherExcelModel teacherExcelModel = new TeacherExcelModel(teacher);
            teacherExcelModel.setRoleList(teacherService.getRoleListByTid(tid).toString().replaceAll("^\\[|\\]$", ""));
            log.info(teacherExcelModel.getRoleList());
            teacherExcelModel.setCollegeName(collegeMapper.getCollegeNameByCid(cid));
            teacherExcelModel.setOfficeName(officeMapper.getOfficeNameByOid(oid));
            IdentityCardAudit identityCardAudit = teacherService.queryIdentityCardShowByTid(tid);
            if (identityCardAudit != null) {
                teacherExcelModel.setIdentityCard(identityCardAudit.getIdNumber());
            }
            return teacherExcelModel;//返回的是TeacherExcelModel的集合
        })).collect(Collectors.toList());

        //自定义标题别名
        log.error(fieldList.toString());
        //ArrayUtil.isNotEmpty(fieldList)
        if (!fieldList.isEmpty()) {
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
            if (fieldList.contains("createDate")) {
                writer.addHeaderAlias("createDate", "注册日期");
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
            writer.addHeaderAlias("createDate", "注册日期");
            writer.merge(13, fileName);
        }
        log.info("teacherExcelList={}", teacherExcelModelList);
        //只写出加了别名的字段
        writer.setOnlyAlias(true);
        writer.write(teacherExcelModelList, true);//标题行true
        return writer;
    }

    @Value("${excel.file-name}")
    String excelFileName;
    private File getTeachersExcelFileByParams(List<Long> ids, List<Long> oids, List<String> fieldList) {
        ExcelWriter writer = this.getExcelWriterByParams(ids, oids, excelFileName, fieldList);
        File excelFile = new File(temporaryPath + sep + excelFileName + ".xlsx");
        writer.flush(excelFile);//直接将数据写入到文件中,并关闭ExcelWriter对象
      /*writer.flush(FileUtil.getOutputStream(excelFile), true);//true 关闭输出流*/
        writer.close();//双重保险
        return excelFile;
    }


}
