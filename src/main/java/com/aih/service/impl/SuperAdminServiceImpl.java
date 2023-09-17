package com.aih.service.impl;

import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.SuperAdminMapper;
import com.aih.service.ISuperAdminService;
import com.aih.custom.exception.CustomException;
import com.aih.custom.exception.CustomExceptionCodeMsg;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void updateAdminStatus(Integer status, List<Long> ids) {
        //开始操作
        log.info("AdminStatus:{},ids:{}",status,ids);
        LambdaUpdateWrapper<Admin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(true,Admin::getId,ids)
                .set(Admin::getStatus, status);
        adminMapper.update(null,updateWrapper);
    }


}
