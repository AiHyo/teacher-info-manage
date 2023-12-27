package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.common.interceptor.AuthAccess;
import com.aih.common.aop_log.LogAnnotation;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Role;
import com.aih.entity.Teacher;
import com.aih.entity.TeacherRole;
import com.aih.entity.vo.AuditInfoDto;
import com.aih.entity.vo.TeacherDto;
import com.aih.mapper.RoleMapper;
import com.aih.mapper.TeacherRoleMapper;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.vo.TeacherDetailDto;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
 * 教师(用户) Controller
 * @author AiH
 * @since 2023-07-07
 */

@Api(tags = {"教师用户接口列表"})
@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private TeacherRoleMapper teacherRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private HttpServletResponse response;
    @Value("${excel.file-name}")
    String defaultExcelName;
    @Value("${file.root-path}")
    String rootPath;
    @Value("${file.temporary-path}")
    String temporaryPath;

    /**
     * 返回包含loginTeacher信息的token,
     */
    //以及loginTeacher的权限信息
    @ApiOperation("教师用户登录")
    @PostMapping("/login")
    public R<Map<String,Object>> login(@RequestBody Teacher teacher)
    {
        log.info("controller teacher:{}",teacher);
        Map<String, Object> data = teacherService.login(teacher);
        return R.success(data);
    }

    //logout还没实现拉黑token
    @ApiOperation("教师用户登出")
    @PostMapping("/logout")
    public R<?> logout(){
        teacherService.logout();
        return R.success("登出成功");
    }

    /**
     * 根据token显示教师自己的有效信息
     */
    @ApiOperation("显示教师自己的有效信息")
    @GetMapping("showInfo")
    public R<TeacherDetailDto> showInfo(){
        Long uid = UserInfoContext.getUser().getId();
        TeacherDetailDto teacherDetailDto = teacherService.queryTeacherDtoByTid(uid);
        return R.success(teacherDetailDto);
    }

    /**
     * 修改自己的密码
     * @param oldPassword 原本密码
     * @param newPassword 新密码
     */
    @ApiOperation("修改密码")
    @PutMapping("/updatePassword")
    public R<?> updatePassword(@RequestParam("oldPassword") String oldPassword,
                               @RequestParam("newPassword") String newPassword) {
        Long uid = UserInfoContext.getUser().getId();
        Teacher teacher = teacherService.getById(uid);
        if (!passwordEncoder.matches(oldPassword, teacher.getPassword())) {
            throw new CustomException(CustomExceptionCodeMsg.OLD_PASSWORD_ERROR);
        }
        //修改密码
        teacher.setPassword(passwordEncoder.encode(newPassword));
        teacherService.updateById(teacher);
        return R.success("修改密码成功");
    }

    /**
     * id验证 oid/cid/isAuditor强制为null防乱改 密码加密
     * @param teacher 需要修改的教师信息 记得传id
     */
    @ApiOperation("修改自己教师信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody Teacher teacher){
        //id必须传
        if (teacher.getId()==null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_IS_NULL);
        }
        //自己才可以修改自己
        if (!UserInfoContext.getUser().getId().equals(teacher.getId())) {
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_SELF);
        }
        //防止乱修改办公室，学院，审核员权限
        teacher.setOid(null);
        teacher.setCid(null);
        teacher.setIsAuditor(null);
        if (teacher.getPassword()!=null) {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
        teacherService.updateById(teacher);
        return R.success("修改教师基础信息成功");
    }
    // == role ==
    @ApiOperation("查看所有的职务")
    @GetMapping("/showRoleList")
    public R<List<Role>> showRoleList(){
        return R.success(roleMapper.selectList(null));
    }
    @ApiOperation("查看自己的职务")
    @GetMapping("/showMyRoleList")
    public R<List<Role>> showMyRoleList(){
        Long tid = UserInfoContext.getUser().getId();
        List<Long> rids = teacherRoleMapper.selectByTid(tid).stream().map(TeacherRole::getRid).collect(Collectors.toList());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId,rids);
        return R.success(roleMapper.selectList(queryWrapper));
    }
    @ApiOperation("修改自己的职务")
    @PutMapping("/updateRole")
    public R<?> updateRole(@RequestParam("rids") List<Long> rids){
        Long tid = UserInfoContext.getUser().getId();
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId,rids);
        List<Role> roleList = roleMapper.selectList(queryWrapper);
        if (roleList.size()!=rids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        //删除tid的所有数据
        teacherRoleMapper.deleteTeacherRoleByTid(tid);
        //插入新的数据
        for (Long rid : rids) {
            teacherRoleMapper.insert(new TeacherRole(tid,rid));
        }
        return R.success("修改角色成功");
    }

    /**
     * test接口 用于测试
     */
    @AuthAccess//放行
    @ApiOperation("新增教师用户")
    @PostMapping
    public R<?> save(@RequestBody Teacher teacher){
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherService.save(teacher);
        return R.success("新增教师用户成功");
    }

    //


    /////////////////////////////////////////////审核员专场/////////////////////////////////////////////
    //////////// ============== 查教师 ================== //////////////

    /**
     * 审核员接口,查询管理教研室下所有的教师,不是审核员会抛出自定义异常,
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param teacherName 教师姓名
     * @param gender 性别
     * @param ethnic 民族
     * @param birthplace 籍贯
     * @param address 住址
     */
    @ApiOperation("[审核员]查询管理教研室下所有的教师")
    @GetMapping("/getTeacherList")
    public R<Page<TeacherDto>> getTeacherList(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(value = "teacherName",required = false) String teacherName,
                                              @RequestParam(value = "gender",required = false)Integer gender,
                                              @RequestParam(value = "ethnic",required = false) String ethnic,
                                              @RequestParam(value = "birthplace",required = false) String birthplace,
                                              @RequestParam(value = "address",required = false) String address){
        this.checkIsAuditor();//检查uid是否为审核员
        return R.success(teacherService.getTeacherList(pageNum,pageSize,teacherName,gender,ethnic,birthplace,address));
    }

    /**
     * tid不存在/没权限会抛出异常
     * @param tid 查询的教师id
     */
    @ApiOperation("[审核员]查看教师详细信息")
    @GetMapping("/getTeacherInfo/{tid}")
    public R<TeacherDetailDto> getTeacherInfo(@PathVariable Long tid) {
        this.checkIsAuditor();//检查uid是否为审核员
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        if (!findTeacher.getOid().equals(UserInfoContext.getUser().getOid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        return R.success(teacherService.queryTeacherDtoByTid(tid));
    }


    //////////// ============== 查审核教师记录 ================== //////////////

    /**
     * 审核员接口,查询管理教研室下所有的教师审核记录。不是审核员/auditStatus不合法,会抛出自定义异常,
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @param auditStatus (可选)审核状态,只接受0/1/2,不传则查询所有
     * @param onlyOwn 布尔类型(可选)只看自己的,默认false
     * @return 每条数据包含：审核类型、审核对象id、审核状态、创建时间、审核时间、教师姓名、教研室名称、学院名称
     */
    @ApiOperation("[审核员][预览]查询管理教研室下所有的教师审核记录")
    @GetMapping("/getAuditList")
    public R<Page<AuditInfoDto>> getAuditList(@RequestParam(value = "pageNum") Integer pageNum,
                                              @RequestParam(value = "pageSize") Integer pageSize,
                                              @RequestParam(value = "auditStatus",required = false) Integer auditStatus,
                                              @RequestParam(value = "onlyOwn",required = false) boolean onlyOwn){
        this.checkIsAuditor();//检查uid是否为审核员
        if(auditStatus!=null){
            MyUtil.checkAuditStatus(auditStatus);//检查auditStatus参数是否合法
        }
        return R.success(teacherService.getAuditList(pageNum,pageSize,auditStatus,onlyOwn));
    }




    //////////// ================== excel ================== //////////////
    /**
     * @param ids       （可选）教师ids
     * @param fileName  （可选）导出excel的名称,默认"教师信息表"
     * @param fieldList 只导出部分字段（可选）id,teacherName,username,gender,identityCard,roleList,ethnic,birthplace,address,phone,collegeName,officeName,isAuditor,createDate
     * @throws IOException
     */
    @LogAnnotation(module = "审核员", operator = "Excel批量导出教师信息")
    @ApiOperation("Excel批量导出教师信息")
    @GetMapping(value = "/export", produces = "application/octet-stream")
//    @AuthAccess
    public void exportExcel(@RequestParam(value = "ids", required = false) List<Long> ids,
                            @RequestParam(value = "fileName", required = false) String fileName,
                            @RequestParam(value = "fieldList", required = false) List<String> fieldList) throws IOException {
        if (StrUtil.isBlank(fileName)) {
            fileName = defaultExcelName;
        }
        List<Teacher> teacherList = this.getTeacherListByIds(ids);
        //获取ExcelWriter excel写入器 渲染好数据的excel
        ExcelWriter writer = teacherService.getMyExcelWriter(teacherList, fileName, fieldList);
        //在浏览器下载：设置response并写出xlsx
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
//        response.setHeader("Content-Length", "" + tempFile.length());

        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);//true 关闭输出流
        writer.close();
        outputStream.flush();
        outputStream.close();
    }
    //////////// ================== word ================== //////////////

    @ApiOperation("导出Word")
    @GetMapping("/exportWord/{tid}")
    public void exportWord(@PathVariable Long tid) throws IOException {
        //判断权限
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        if (!findTeacher.getOid().equals(UserInfoContext.getUser().getOid())) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        //获取XWPFTemplate word写入器 渲染好数据的word
        XWPFTemplate render = teacherService.getWordRender(findTeacher);
        //在浏览器下载：设置response并写出docx
        String fileName = URLEncoder.encode(findTeacher.getTeacherName() + "教师信息表", "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".docx");
        ServletOutputStream outputStream = response.getOutputStream();
        render.writeAndClose(outputStream);
        render.close();//双重保障
        outputStream.flush();
        outputStream.close();
    }

    //////////// ================== 打包 ================== //////////////
    /**
     * 可选参数限制导出内容,不选就默认所有
     * @param ids 教师id
     * @param fieldList      字段
     * @param attachmentList 附件种类
     */
    @ApiOperation("导出压缩包")
    @GetMapping("/exportZip/")
    @LogAnnotation(module="管理员",operator="导出压缩包")
    public void exportZip(@RequestParam(value = "ids", required = false) List<Long> ids,
                          @RequestParam(value = "fieldList", required = false) List<String> fieldList,
                          @RequestParam(value = "attachmentList", required = false) List<String> attachmentList) throws IOException {
        ///////////////////////////////////////////////////////////if
        //创建一个新的临时文件路径！！！
        if (FileUtil.exist(temporaryPath)) {
            FileUtil.del(temporaryPath);
        }//创建临时zip包
        File tempZipFile = new File(temporaryPath + File.separator + "test压缩.zip");
        //需要压缩的文件列表
        List<File> fileList = CollUtil.newArrayList();
        //调用封装函数获取教师列表
        List<Teacher> teacherList = this.getTeacherListByIds(ids);
        //添加需要压缩的文件
        fileList.add(teacherService.getTeacherAttachmentFolder(teacherList,attachmentList));//教师附件文件夹
        fileList.add(teacherService.getMyExcelFile(teacherList, fieldList)); //excel
        fileList.add(teacherService.getMyWordFolder(teacherList));           //word
        fileList.add(new File(rootPath + File.separator + "qwq.jpg"));
        //开始压缩
        ZipUtil.zip(tempZipFile, true, fileList.toArray(new File[fileList.size()]));
        //下载zip到浏览器！！！
        MyUtil.downloadZip(tempZipFile, response);
        //统一删除所有临时文件！！！！！！
        FileUtil.del(temporaryPath);
    }


    //==========================================封装方法=============================================
    private List<Teacher> getTeacherListByIds(List<Long> ids) { // 传入ids,先检验,再获取teacherList
        Long u_oid = UserInfoContext.getUser().getOid();
//        Long u_oid = 1L;//测试用
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        /*权限只有自己管理学院*/
        queryWrapper.eq(Teacher::getOid, u_oid);
        //根据传参判断选择的教师
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                Teacher teacher = teacherService.getById(id);
                if (teacher == null) {
                    throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
                }
                if (!teacher.getCid().equals(u_oid)) {
                    throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
                }
            }//没有非法id就根据ids选
            queryWrapper.in(Teacher::getId, ids);
        } else log.info("没有选择指定教师,默认选择管理办公室下所有教师");
        return teacherService.list(queryWrapper);

    }

    private void checkIsAuditor() { //检查uid是否为审核员
        Long tid = UserInfoContext.getUser().getId();
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher.getIsAuditor()==0) {
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_AUDITOR);
        }
    }

    //查询自己所有的审核记录
//    @ApiOperation("查询自己所有的审核记录")
//    @GetMapping("/showAuditList")
//    //返回类型是不同实体类型组成的列表
//    public R<List<?>> showAuditList(@RequestHeader("token") String token){
////        List<?> list = teacherService.showAuditList(token);
//        return R.success(list);

//    }




}
