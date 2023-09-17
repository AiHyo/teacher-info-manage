package com.aih.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.aih.custom.annotation.AuthAccess;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.entity.Teacher;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IAdminService;
import com.aih.service.ISuperAdminService;
import com.aih.service.ITeacherService;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    @ApiOperation("修改管理员学院权限")
    @PutMapping("/updateAdminCollegePower/{cid}")
    public R<?> updateAdminCid(@PathVariable Long cid,@RequestParam Long id){
        Admin admin = adminService.getById(id);
        admin.setCid(cid);
        adminService.updateById(admin);
        return R.success("修改成功");
    }

    /**
     * ids非法 抛出自定义异常
     */
    @ApiOperation("可批量启用/禁用管理员")
    @PostMapping("/updateAdminStatus/{status}")
    public R<?> updateAdminStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
        //判断ids是否合法
        LambdaQueryWrapper<Admin> queryWrapper_ids = new LambdaQueryWrapper<>();
        queryWrapper_ids.in(Admin::getId, ids);
        long count = adminService.count(queryWrapper_ids);
        if (count!=ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        superAdminService.updateAdminStatus(status,ids);
        return R.success("修改成功");
    }

    //登出
    @ApiOperation("超级管理员登出")
    @PostMapping("/logout")
    public R<?> logout(){
        superAdminService.logout();
        return R.success("登出成功");
    }


}
