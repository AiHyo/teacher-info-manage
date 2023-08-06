package com.aih.controller;

import com.aih.entity.Admin;
import com.aih.entity.Teacher;
import com.aih.service.IAdminService;
import com.aih.utils.vo.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 登录成功返回token
     */
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public R<Map<String,Object>> login(@RequestBody Admin admin)
    {
        Map<String, Object> data = adminService.login(admin);
        return R.success(data);
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/showInfo")
    public R<Admin> showInfo(){
        Admin admin = adminService.showInfo();
        return R.success(admin);
    }


    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody Admin admin){
        adminService.updateById(admin);
        return R.success("修改成功");
    }

    /**
     *  路径参数 0:设为非审核员  1:设为审核员  传入ids/token非法 抛出自定义异常
     */
    @ApiOperation("批量修改审核员权限")
    @PostMapping("/auditorPower/{isAuditor}")
    public R<String> auditorPower(@PathVariable Integer isAuditor, @RequestParam List<Long> ids)
    {
        adminService.updateIsAuditor(isAuditor,ids);
        return R.success("更改审核员权限成功");
    }

    @ApiOperation("查询管理学院下所有的教师")
    @GetMapping("/getTeacherListByCid")
    public R<List<Teacher>> getTeacherByCid(){
        List<Teacher> teachers = adminService.getTeacherListByCid();
        return R.success(teachers);
    }

    //登出
    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public R<?> logout(){
        adminService.logout();
        return R.success("登出成功");
    }


}
