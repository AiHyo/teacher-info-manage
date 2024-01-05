package com.aih.controller;

import cn.hutool.core.collection.CollUtil;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.entity.Role;
import com.aih.entity.Teacher;
import com.aih.entity.TeacherRole;
import com.aih.mapper.RoleMapper;
import com.aih.mapper.TeacherRoleMapper;
import com.aih.service.ITeacherService;
import com.aih.utils.UserInfoContext;
import com.aih.utils.vo.R;
import com.aih.utils.vo.RoleType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AiH
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private TeacherRoleMapper teacherRoleMapper;

    @ApiOperation("查看所有的职务")
    @GetMapping("/getAllRole")
    public R<List<Role>> showRoleList(){
        return R.success(roleMapper.selectList(null));
    }

    /**
     * 根据tid查看教师的职务
     * @param tid 教师id 不传则默认查看自己的(教师才可以操作)
     * @return 教师的职务列表
     */
    @ApiOperation("[管理员/教师自己]查看教师的职务")
    @GetMapping("/getRoleListByTid")
    public R<List<Role>> showRoleListByTid(@RequestParam(value = "tid",required = false) Long tid){
        if (tid==null) {//不传,则查看自己
            if (UserInfoContext.getUser().getRoleType()!=RoleType.TEACHER) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
            }
            tid= UserInfoContext.getUser().getId();
        }else{ //传了,则管理员查教师
            if (UserInfoContext.getUser().getRoleType()!=RoleType.ADMIN) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
            }
            this.checkTeacherIds(CollUtil.newArrayList(tid));//检验tid是否存在/有无权限
        }
        // == 开始查询 ==
        List<Long> rids = teacherRoleMapper.selectByTid(tid).stream().map(TeacherRole::getRid).collect(Collectors.toList());
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Role::getId,rids);
        return R.success(roleMapper.selectList(queryWrapper));
    }


    /** 根据tid和rids修改教师的职务。检验有无对tid的权限,rids是否都存在
     * @param tid 教师id 不传则默认修改自己的(教师才可以操作)
     * @param rids 职务id列表
     */
    @ApiOperation("[管理员][教师自己]修改教师的职务")
    @PutMapping({"/updateRole"})
    public R<?> updateRole(@RequestParam(value = "tid",required = false) Long tid, @RequestParam("rids") List<Long> rids){
        if (tid==null) {//不传,则修改自己
            if (UserInfoContext.getUser().getRoleType()!=RoleType.TEACHER) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
            }
            tid = UserInfoContext.getUser().getId();
        } else { //传了,则管理员修改教师
            if (UserInfoContext.getUser().getRoleType()!=RoleType.ADMIN) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
            }
            this.checkTeacherIds(CollUtil.newArrayList(tid)); //检验tid是否存在/有无权限
        }
        // == 开始修改 ==
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

    //==========================================封装方法=============================================
    private void checkTeacherIds(List<Long> ids) {
        //判断ids是否全部都存在
        LambdaQueryWrapper<Teacher> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.in(Teacher::getId, ids);
        long count = teacherService.count(queryWrapper_1);
        if (count != ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        //判断ids是否都是当下学院的(权限
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Teacher::getId, ids);
        List<Teacher> teachers = teacherService.list(queryWrapper);
        for (Teacher teacher : teachers) {
            if (!teacher.getCid().equals(cid)) {
                throw new CustomException(CustomExceptionCodeMsg.UPDATE_AUDIT_POWER_ERROR);
            }
        }
    }
}
