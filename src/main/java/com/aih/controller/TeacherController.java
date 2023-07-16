package com.aih.controller;

import com.aih.utils.vo.R;
import com.aih.entity.dto.TeacherDto;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.entity.Teacher;
import com.aih.service.ITeacherService;
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
        Map<String, Object> data = teacherService.login(teacher);
        log.info("data:{}",data);
        if (data!=null){
            return R.success(data);
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    //logout还没实现拉黑token
    @ApiOperation("教师用户登出")
    @PostMapping("/logout")
    public R<?> logout(@RequestHeader("token") String token){
        teacherService.logout(token);
        return R.success("登出成功");
    }

    /**
     * 根据token显示教师有效的信息
     */
    @ApiOperation("根据token显示教师有效的信息")
    @GetMapping("showInfo")
    public R<TeacherDto> getTeacherInfoByToken(@RequestHeader("token") String token){
        TeacherDto teacherDto = teacherService.getTeacherInfoByToken(token);
//        if (teacherDto!=null){
//            return R.success(teacherDto);
//        }
//        throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
        return R.success(teacherDto);
    }

    @ApiOperation("新增教师用户")
    @PostMapping
    public R<?> save(@RequestBody Teacher teacher){
        try {
            teacher.setPwd(passwordEncoder.encode(teacher.getPwd()));
            log.info("teacher:{}",teacher);
            teacherService.save(teacher);
            return R.success("新增教师用户成功");
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCodeMsg.SAVE_TEACHER_ERROR);
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
