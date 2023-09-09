package com.aih.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aih.entity.Admin;
import com.aih.entity.Teacher;
import com.aih.entity.dto.TeacherDto;
import com.aih.service.IAdminService;
import com.aih.service.ITeacherService;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
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
    @Autowired
    private ITeacherService teacherService;

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

    /**
        可选模糊查询 keyword: 教研室/教师名称
     */
    @ApiOperation("查询管理学院下所有的教师")
    @GetMapping("/getTeacherList")
    public R<Page<Teacher>> getTeacherList(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam(value = "keyword",required = false) String keyword,
                                            @RequestParam(value = "isAuditor",required = false) Integer isAuditor,
                                            @RequestParam(value = "gender",required = false)Integer gender,
                                            @RequestParam(value = "ethnic",required = false) String ethnic,
                                            @RequestParam(value = "birthplace",required = false) String birthplace,
                                            @RequestParam(value = "address",required = false) String address){
        Page<Teacher> pageInfo = new Page<>(pageNum,pageSize);
        return R.success(adminService.getTeacherList(pageInfo,keyword,isAuditor,gender,ethnic,birthplace,address));
    }


    @ApiOperation("查看教师详细信息")
    @GetMapping("/getTeacherInfo/{tid}")
    public R<TeacherDto> getTeacherInfo(@PathVariable Long tid){
        //判断权限
        Teacher findTeacher = teacherService.getById(tid);
        if(findTeacher==null){
            throw new CustomException(CustomExceptionCodeMsg.PATH_PARAM_ILLEGAL);
        }
        if (!findTeacher.getCid().equals(UserInfoContext.getUser().getCid())){
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        return R.success(teacherService.queryTeacherDtoByTid(tid));
    }

    /**
     *  批量导出数据
     */
    @ApiOperation("Excel批量导出教师信息")
    @GetMapping("/export")
    public R<?> export(@RequestParam(value = "username",required = false) String username,
                       @RequestParam(value = "teacherName",required = false) String teacherName,
                       @RequestParam(value = "ids",required = false) List<Long> ids,
                       HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<Teacher> teacherList;
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        if (ids!=null){
            queryWrapper.in(Teacher::getId,ids);
        }else {
            queryWrapper.like(StrUtil.isNotBlank(username),Teacher::getUsername,username);
            queryWrapper.like(StrUtil.isNotBlank(teacherName),Teacher::getTeacherName,teacherName);
        }
        teacherList = teacherService.list(queryWrapper);
        writer.write(teacherList,true);//标题行true
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
        return R.success("导出成功");
    }


    //登出
    @ApiOperation("管理员登出")
    @PostMapping("/logout")
    public R<?> logout(){
        adminService.logout();
        return R.success("登出成功");
    }




}
