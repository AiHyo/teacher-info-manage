package com.aih.service.impl;

import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.entity.Teacher;
import com.aih.entity.vo.TeacherVo;
import com.aih.mapper.*;
import com.aih.service.ISuperAdminService;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 超级管理员 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class SuperAdminServiceImpl extends ServiceImpl<SuperAdminMapper, SuperAdmin> implements ISuperAdminService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CollegeMapper collegeMapper;

    @Override
    public Map<String, Object> login(SuperAdmin superAdmin) {
        //根据教师用户名查询
        log.info("superAdmin传入参数:{}",superAdmin);
        LambdaQueryWrapper<SuperAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperAdmin::getUsername,superAdmin.getUsername());
        SuperAdmin loginSuperAdmin = this.baseMapper.selectOne(queryWrapper);
        log.info("找到的superAdmin:{}",loginSuperAdmin);
        //如果不为空,并且密码和传入密码相匹配,则生成token
        if(loginSuperAdmin!=null && passwordEncoder.matches(superAdmin.getPassword(),loginSuperAdmin.getPassword()))
        {
            loginSuperAdmin.setPassword(null);//清空密码
            String token = jwtUtil.createToken(loginSuperAdmin,"SuperAdmin");
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            data.put("superadminName",loginSuperAdmin.getSuperadminName());
            return data;
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    @Override
    public SuperAdmin showInfo() {
        Long id = UserInfoContext.getUser().getId();
        /*if(!String.valueOf(id).startsWith("3")){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_SUPERADMIN);
        }*/
        SuperAdmin superAdmin = this.baseMapper.selectById(id);
        superAdmin.setPassword(null);//将密码置空
        return superAdmin;
    }

    @Override
    public void logout() {

    }

    @Override
    public void updateAdminsCid(Long cid, List<Long> ids) {
        log.info("cid:{},ids:{}",cid,ids);
        //获取 cid变了的 new_ids, 只修改变动的,因为会修改 权限生效时间createDate
        List<Long> new_ids = ids.stream().filter(id -> {
            Admin admin = adminMapper.selectById(id);
            return !admin.getCid().equals(cid); // 返回不相等的
        }).collect(Collectors.toList());
        if (new_ids.isEmpty()){
            return;
        }
        //开始操作
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Admin::getId,new_ids)
                .set(Admin::getCid, cid)
                .set(Admin::getCreateDate, LocalDateTime.now()); //修改角色权限之后新的创建时间
        adminMapper.update(null,updateWrapper);//null表示不修改其他字段
    }

    @Override
    public void resetPassword(List<Long> ids, String password) {
        log.info("重置密码的ids:{}",ids);
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Admin::getId,ids)
                .set(Admin::getPassword, passwordEncoder.encode(password));
        adminMapper.update(null,updateWrapper);
    }

    @Override
    public Page<TeacherVo> getTeacherList(Integer pageNum, Integer pageSize, Long cid) {
        Page<Teacher> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(cid!=null,Teacher::getCid,cid);
        teacherMapper.selectPage(page,queryWrapper);
        List<TeacherVo> collect = page.getRecords().stream().map((item) -> {
            TeacherVo teacherDto = new TeacherVo();
            BeanUtils.copyProperties(item, teacherDto);//复制item给TeacherDto
            teacherDto.setOfficeName(officeMapper.getOfficeNameByOid(item.getOid()));
            teacherDto.setCollegeName(collegeMapper.getCollegeNameByCid(item.getCid()));
            List<String> roleNameByTeacherId = teacherMapper.getRoleNameByTeacherId(item.getId());
            teacherDto.setRoleList(String.join(",", roleNameByTeacherId));
            return teacherDto;
        }).collect(Collectors.toList());
        Page<TeacherVo> dtoPage = new Page<>(pageNum, pageSize);
        BeanUtils.copyProperties(page,dtoPage,"records");
        dtoPage.setRecords(collect);
        return dtoPage;
    }

    @Override
    public void updateAdminsStatus(Integer status, List<Long> ids) {
        //开始操作
        log.info("AdminStatus:{},ids:{}",status,ids);
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Admin::getId,ids)
                .set(Admin::getStatus, status);
        adminMapper.update(null,updateWrapper);
    }




}
