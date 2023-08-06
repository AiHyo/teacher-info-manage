package com.aih.service.impl;

import com.aih.entity.Admin;
import com.aih.entity.Teacher;
import com.aih.entity.dto.TeacherDto;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.service.IAdminService;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author AiH
 * @since 2023-07-07
 */
@Service
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public Map<String, Object> login(Admin admin) {
        //根据教师用户名查询
        log.info("admin传入参数:{}",admin);
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,admin.getUsername());
        Admin loginAdmin = this.baseMapper.selectOne(queryWrapper);
        log.info("找到的loginAdmin:{}",loginAdmin);
        //如果不为空,并且密码和传入密码相匹配,则生成token
        if(loginAdmin!=null && passwordEncoder.matches(admin.getPassword(),loginAdmin.getPassword()))
        {
            if (loginAdmin.getStatus() == 0) {
                throw new CustomException(CustomExceptionCodeMsg.USER_IS_DISABLED);
            }
            loginAdmin.setPassword(null);//清空密码
            String token = jwtUtil.createToken(loginAdmin,"Admin");
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        throw new CustomException(CustomExceptionCodeMsg.USERNAME_OR_PASSWORD_ERROR);
    }

    @Override
    public Admin showInfo() {
        Long id = UserInfoContext.getUser().getId();
        /*if(!String.valueOf(id).startsWith("2")){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
        }*/
        Admin loginAdmin = this.baseMapper.selectById(id);
        loginAdmin.setPassword(null);//将密码置空
        return loginAdmin;
    }

    @Override
    public void updateIsAuditor(Integer isAuditor, List<Long> ids) {
        /*if (!String.valueOf(UserInfoContext.getUser().getId()).startsWith("2")){
            throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
        }*/
        //判断ids是否合法
        LambdaQueryWrapper<Teacher> queryWrapper_1 = new LambdaQueryWrapper<>();
        queryWrapper_1.in(Teacher::getId, ids);
        Long count = teacherMapper.selectCount(queryWrapper_1);
        if (count!=ids.size()) {
            throw new CustomException(CustomExceptionCodeMsg.IDS_ILLEGAL);
        }
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Teacher::getId, ids);
        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);
        for (Teacher teacher : teachers) {
            if (!teacher.getCid().equals(cid)) {
                throw new CustomException(CustomExceptionCodeMsg.UPDATE_AUDIT_POWER_ERROR);
            }
        }
        //开始操作
        log.info("isAuditor:{},ids:{}",isAuditor,ids);
        LambdaUpdateWrapper<Teacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids!=null,Teacher::getId,ids)
                .set(Teacher::getIsAuditor, isAuditor);
        teacherMapper.update(null,updateWrapper);
    }

    @Override
    public void logout() {

    }

    @Override
    public List<Teacher> getTeacherListByCid() {
        Long cid = UserInfoContext.getUser().getCid();
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getCid,cid);
        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);
        return teachers;
    }



}
