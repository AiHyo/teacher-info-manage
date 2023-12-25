package com.aih.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.aih.common.interceptor.AuthAccess;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.*;
import com.aih.entity.vo.OfficeDto;
import com.aih.service.*;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 超级管理员 Controller
 *
 * @author AiH
 * @since 2023-07-07
 */
@RestController
@RequestMapping("/superAdmin")
public class SuperAdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ISuperAdminService superAdminService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private ICollegeService collegeService;
    @Autowired
    private IOfficeService officeService;
    @Value("${default-password}")
    private String defaultPassword;
    /**
     * test 创建超管
     */
    @AuthAccess
    @ApiOperation("创建超级管理员")
    @PostMapping("/create")
    public R<?> save(@RequestBody SuperAdmin superAdmin){
        superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        superAdminService.save(superAdmin);
        return R.success("创建成功");
    }

    @ApiOperation("Excel批量导入教师信息")
    @PostMapping("/import")
    @Transactional(rollbackFor=Exception.class) //事务回滚
    public R<?> importExcel(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<Teacher> teacherList = reader.readAll(Teacher.class);
        try {
            teacherService.saveBatch(teacherList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CustomExceptionCodeMsg.EXCEL_IMPORT_ERROR);
        }
        return R.success("导入成功");
    }

    /**
     * 登录成功返回token
     */
    @ApiOperation("超级管理员登录")
    @PostMapping("/login")
    public R<Map<String,Object>> login(@RequestBody SuperAdmin superAdmin)
    {
        Map<String, Object> data = superAdminService.login(superAdmin);
        return R.success(data);
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
        SuperAdmin superAdmin = superAdminService.getById(uid);
        if (!passwordEncoder.matches(oldPassword, superAdmin.getPassword())) { // 旧密码不正确
            throw new CustomException(CustomExceptionCodeMsg.OLD_PASSWORD_ERROR);
        }
        //修改密码
        superAdmin.setPassword(passwordEncoder.encode(newPassword));
        superAdminService.updateById(superAdmin);
        return R.success("修改密码成功");
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/showInfo")
    public R<SuperAdmin> showInfo(){
        SuperAdmin admin = superAdminService.showInfo();
        return R.success(admin);
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody SuperAdmin superAdmin){
        superAdminService.updateById(superAdmin);
        return R.success("修改成功");
    }

    @ApiOperation("获取管理员列表")
    @GetMapping("/getAdminList")
    public R<List<Admin>> getAdminList(){
        List<Admin> adminList = adminService.list();//查询所有管理员
        return R.success(adminList);
    }

    /**
     * status默认1表示启用
     */
    @ApiOperation("添加管理员")
    @PostMapping("/addAdmin")
    public R<?> addAdmin(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminService.save(admin);
        return R.success("添加成功");
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/deleteAdmin/{id}")
    public R<?> deleteAdmin(@PathVariable Long id){
        adminService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * cid/ids非法 抛出自定义异常。 权限改动了的管理员,权限生效时间createDate会被更新成当前时间
     * @param ids 管理员id列表
     * @param cid 学院id, 对应修改的权限
     * @return
     */
    @ApiOperation("(批量)修改管理员学院权限")
    @PutMapping("/updateAdminCollegePower/{cid}")
    public R<?> updateAdminCid(@PathVariable Long cid,@RequestParam List<Long> ids){
        //判断cid是否合法
        LambdaQueryWrapper<College> queryWrapper_cid = new LambdaQueryWrapper<>();
        queryWrapper_cid.eq(College::getId,cid);
        long count_cid = collegeService.count(queryWrapper_cid);
        if (count_cid!=1) {
            throw new CustomException(CustomExceptionCodeMsg.CID_ILLEGAL);
        }
        //判断ids是否合法
        this.checkAdminIds(ids);
        superAdminService.updateAdminsCid(cid,ids);
        return R.success("修改管理员权限成功");
    }

    /**
     * ids非法 抛出自定义异常
     * @param ids 管理员id列表
     * @param password (可选)重置后的密码,不传默认123456
     */
    @ApiOperation("(批量)重置密码")
    @PutMapping("/resetPassword")
    public R<?> resetPassword(@RequestParam List<Long> ids,@RequestParam(required = false) String password){
        if (StrUtil.isBlank(password)) {
            password = defaultPassword;
        }
        //判断ids是否合法
        this.checkAdminIds(ids);
        superAdminService.resetPassword(ids,password);
        return R.success("重置密码成功");
    }


    /**
     * status/ids若非法 抛出自定义异常
     * @param status 0禁用 1启用
     * @param ids 管理员id列表
     */
    @ApiOperation("(批量)启用/禁用管理员")
    @PostMapping("/updateAdminStatus/{status}")
    public R<?> updateAdminStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        //判断status是否合法
        if (status!=0 && status!=1) {
            throw new CustomException(CustomExceptionCodeMsg.STATUS_ILLEGAL);
        }
        //判断ids是否合法
        this.checkAdminIds(ids);
        superAdminService.updateAdminsStatus(status,ids);
        return R.success("修改成功");
    }

    //添加办公室,学院
    // ============================= 学院 =============================
    @ApiOperation("查看所有学院")
    @GetMapping("/getAllCollege")
    public R<Page<College>> getAllCollege(@RequestParam("pageNum") Integer pageNum,
                                          @RequestParam("pageSize") Integer pageSize){
        Page<College> page = new Page<>(pageNum, pageSize);
        return R.success(collegeService.page(page));
    }
    /**
     * @param collegeName 学院名称
     */
    @ApiOperation("添加学院")
    @PostMapping("/addCollege")
    public R<?> addCollege(@RequestParam("collegeName") String collegeName){
        College college = new College();
        college.setCollegeName(collegeName);
        collegeService.save(college);
        return R.success("添加成功");
    }

    /**
     * 根据id删除学院. id不存在/有教师属于该学院,抛出自定义异常信息
     * @param id 学院的id
     */
    @ApiOperation("删除学院")
    @DeleteMapping("/deleteCollege/{id}")
    public R<?> deleteCollege(@PathVariable Long id){
        //id存在
        College college = collegeService.getById(id);
        if (college == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        //判断没有教师是该学院的才可以删除
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getCid,id);
        long count = teacherService.count(queryWrapper);
        if (count!=0) {
            throw new CustomException(CustomExceptionCodeMsg.COLLEGE_DELETE_ERROR);
        }
        collegeService.removeById(id);
        return R.success("删除成功");
    }

    // ============================= 办公室 =============================

    /**
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param officeName 办公室名称(可选),根据officeName模糊查询
     */
    @ApiOperation("查看所有办公室")
    @GetMapping("/getAllOffice")
    public R<Page<OfficeDto>> getAllOfficeDto(@RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(required = false) String officeName){
        return R.success(officeService.getAllOffice(pageNum,pageSize, officeName));
    }
    /**
     * 添加办公室,需要officeName办公室名称 & cid学院id。 检验cid是否存在,不存在抛出异常
     * @param college officeName & cid 主要是cid(需要是存在的)
     */
    @ApiOperation("添加办公室")
    @PostMapping("/addOffice")
    public R<?> addOffice(@RequestBody Office college){
        Long cid = college.getCid();
        College findCollege = collegeService.getById(cid);//判断cid是否存在
        if (findCollege == null) {
            throw new CustomException(CustomExceptionCodeMsg.CID_ILLEGAL);
        }
        officeService.save(college);
        return R.success("添加成功");
    }

    /**
     * 根据id删除办公室,id不存在/有教师属于该办公室,抛出自定义异常信息
     * @param id 办公室的id
     */
    @ApiOperation("删除办公室")
    @DeleteMapping("/deleteOffice/{id}")
    public R<?> deleteOffice(@PathVariable Long id){
        //id存在
        Office office = officeService.getById(id);
        if (office == null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_NOT_EXIST);
        }
        //判断没有教师是该办公室的才可以删除
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getOid,id);
        long count = teacherService.count(queryWrapper);
        if (count!=0) {
            throw new CustomException(CustomExceptionCodeMsg.OFFICE_DELETE_ERROR);
        }
        collegeService.removeById(id);
        return R.success("删除成功");
    }


    //登出
    @ApiOperation("超级管理员登出")
    @PostMapping("/logout")
    public R<?> logout(){
        superAdminService.logout();
        return R.success("登出成功");
    }

    //封装一个判断管理员的ids是否存在
    private void checkAdminIds(List<Long> ids){
        LambdaQueryWrapper<Admin> queryWrapper_ids = new LambdaQueryWrapper<>();
        queryWrapper_ids.in(Admin::getId, ids);
        long count = adminService.count(queryWrapper_ids);
        if (count!=ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
    }

}
