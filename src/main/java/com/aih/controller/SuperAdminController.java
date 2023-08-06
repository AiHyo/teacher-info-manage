package com.aih.controller;

import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.service.IAdminService;
import com.aih.service.ISuperAdminService;
import com.aih.utils.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
    /**
     * test 创建超管
     */
    @ApiOperation("创建超级管理员")
    @PostMapping("/create")
    public R<?> save(@RequestBody SuperAdmin superAdmin){
        superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        superAdminService.save(superAdmin);
        return R.success("创建成功");
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
    @ApiModelProperty("可批量启用/禁用管理员")
    @PostMapping("/updateAdminStatus/{status}")
    public R<?> updateAdminStatus(@PathVariable Integer status, @RequestParam List<Long> ids){
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
