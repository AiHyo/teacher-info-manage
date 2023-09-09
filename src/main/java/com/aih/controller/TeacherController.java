package com.aih.controller;

import com.aih.utils.AuthAccess;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.dto.TeacherDto;
import com.aih.entity.Teacher;
import com.aih.service.ITeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private PasswordEncoder passwordEncoder;

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
    public R<TeacherDto> showInfo(){
        Long uid = UserInfoContext.getUser().getId();
        TeacherDto teacherDto = teacherService.queryTeacherDtoByTid(uid);
        return R.success(teacherDto);
    }

    @ApiOperation("修改教师信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody Teacher teacher){
        teacherService.updateById(teacher);
        return R.success("修改教师基础信息成功");
    }

    @AuthAccess//放行
    @ApiOperation("新增教师用户")
    @PostMapping
    public R<?> save(@RequestBody Teacher teacher){
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherService.save(teacher);
        return R.success("新增教师用户成功");
    }

    /**
        可选模糊查询 keyword: 教师名称
     */
    @ApiOperation("查询管理教研室下所有的教师")
    @GetMapping("/getTeacherList")
    public R<Page<Teacher>> getTeacherList(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam(value = "teacherName",required = false) String teacherName,
                                           @RequestParam(value = "gender",required = false)Integer gender,
                                           @RequestParam(value = "ethnic",required = false) String ethnic,
                                           @RequestParam(value = "birthplace",required = false) String birthplace,
                                           @RequestParam(value = "address",required = false) String address){
        Long tid = UserInfoContext.getUser().getId();
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher.getIsAuditor()==0) {
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_AUDITOR);
        }
        Page<Teacher> pageInfo = new Page<>(pageNum, pageSize);
        return R.success(teacherService.getTeacherList(pageInfo,pageNum,pageSize,teacherName,gender,ethnic,birthplace,address));
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
