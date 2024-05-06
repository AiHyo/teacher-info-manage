package com.aih.controller;

import com.aih.common.interceptor.AuthAccess;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.common.interceptor.AuthTokeAccess;
import com.aih.entity.Teacher;
import com.aih.entity.dto.TeacherDto;
import com.aih.mapper.TeacherRoleMapper;
import com.aih.service.IRoleService;
import com.aih.utils.MyUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.entity.vo.TeacherDetailVo;
import com.aih.service.ITeacherService;
import com.aih.utils.vo.RoleType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    private IRoleService roleService;
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
     * 通用接口,根据tid查询教师详细信息,如果不传tid则默认查询自己的信息. tid不存在/没权限会抛出对应错误信息
     * @param tid
     */
    @AuthTokeAccess // 通用,传入token放行
    @ApiOperation("查看教师详细信息")
    @GetMapping("getTeacherDetailInfo/")
    public R<TeacherDetailVo> getTeacherDetailInfo(@RequestParam(value = "tid",required = false) Long tid){
        log.info("tid:{}",tid);
        if (tid==null) {
            Long uid = UserInfoContext.getUser().getId();
            tid = uid;
        }
        log.info("==============",tid);
        Teacher findTeacher = teacherService.getById(tid);
        if (findTeacher == null) {
            throw new CustomException(CustomExceptionCodeMsg.NOT_FOUND_TEACHER);
        }
        RoleType roleType = UserInfoContext.getUser().getRoleType();
        if ( (roleType == RoleType.AUDITOR && !findTeacher.getOid().equals(UserInfoContext.getUser().getOid()))
            || (roleType == RoleType.ADMIN && !findTeacher.getCid().equals(UserInfoContext.getUser().getCid()))) {
            throw new CustomException(CustomExceptionCodeMsg.POWER_NOT_MATCH);
        }
        return R.success(teacherService.queryTeacherDetailDtoByTid(tid));
    }
    /**
     * 通用接口 传token就行
     */
    @ApiOperation("查看每个学院的教师人数")
    @AuthTokeAccess
    @GetMapping("/getTeacherCountByCollege")
    public R<?> getTeacherCountByCollege(){
        List<Map<String, Object>> teacherCountByCollege = teacherService.getTeacherCountByCollege();
        return R.success(teacherCountByCollege);
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
     * id验证 oid/cid/isAuditor强制为null防乱改 密码加密,检验手机号
     * @param teacherDto 需要修改的教师信息 记得传id
     */
    @ApiOperation("修改自己教师信息")
    @PutMapping("/update")
    public R<?> update(@RequestBody TeacherDto teacherDto){
        Long tid = teacherDto.getId();
        //id必须传
        if (tid ==null) {
            throw new CustomException(CustomExceptionCodeMsg.ID_IS_NULL);
        }
        //自己才可以修改自己
        if (!UserInfoContext.getUser().getId().equals(tid)) {
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_SELF);
        }
        //防止乱修改办公室，学院，审核员权限
        teacherDto.setOid(null);
        teacherDto.setCid(null);
        teacherDto.setIsAuditor(null);
        if (teacherDto.getPassword()!=null) {
            teacherDto.setPassword(passwordEncoder.encode(teacherDto.getPassword()));
        }
        MyUtil.checkPhone(teacherDto.getPhone());//检验手机号格式
        List<Long> roleIds = teacherDto.getRids();
        //判断roleIds不为空
        if (roleIds!=null && !roleIds.isEmpty()){
            roleService.updateTeacherRole(tid,roleIds);
        }
        Teacher teacher = new Teacher();
        teacher.setId(tid);
        teacher.setTeacherName(teacherDto.getTeacherName());
        teacher.setUsername(teacherDto.getUsername());
        teacher.setPassword(teacherDto.getPassword());
        teacher.setGender(teacherDto.getGender());
        teacher.setEthnic(teacherDto.getEthnic());
        teacher.setNativePlace(teacherDto.getNativePlace());
        teacher.setAddress(teacherDto.getAddress());
        teacher.setPhone(teacherDto.getPhone());
        teacher.setPoliticsStatus(teacherDto.getPoliticsStatus());
        teacher.setEducationDegree(teacherDto.getEducationDegree());
        teacher.setIdNumber(teacherDto.getIdNumber());
        teacher.setStartDate(teacherDto.getStartDate());
        teacher.setCreateDate(teacherDto.getCreateDate());
        teacher.setDeleted(teacherDto.getDeleted());
        teacherService.updateById(teacher);
        return R.success("修改教师基础信息成功");
    }


    /**
     * test接口 用于测试
     */
    @AuthAccess//放行
    @ApiOperation("test新增教师用户")
    @PostMapping
    public R<?> save(@RequestBody Teacher teacher){
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherService.save(teacher);
        return R.success("新增教师用户成功");
    }



}
