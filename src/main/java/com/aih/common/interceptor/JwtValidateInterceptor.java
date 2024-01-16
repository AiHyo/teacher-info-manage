package com.aih.common.interceptor;

import com.aih.controller.AdminController;
import com.aih.controller.SuperAdminController;
import com.aih.controller.TeacherController;
import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.mapper.AdminMapper;
import com.aih.mapper.SuperAdminMapper;
import com.aih.mapper.TeacherMapper;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.Teacher;
import com.aih.common.exception.CustomException;
import com.aih.common.exception.CustomExceptionCodeMsg;
import com.aih.utils.vo.RoleType;
import com.aih.utils.vo.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private SuperAdminMapper superAdminMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ========================== 无需token的请求 ==========================
        if(request.getRequestURI().contains("login")){
            return true;//登录请求直接放行
        }
        if (handler instanceof HandlerMethod) {
            AuthAccess methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if(methodAnnotation != null){
                return true;//有AuthAccess注解的方法直接放行 [无需token]
            }
        }
        // ========================== 需要token的请求 ==========================
        String token  = request.getHeader("token");
/*        log.debug(request.getRequestURI() + "需要验证： " + token);
        log.debug("================================token{}",token);*/
        if(token == null){
            throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
        }
        //解析token创建线程变量
        this.parseTokenAndMakeUser(token,request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;//强转为HandlerMethod
            // == AuthTokeAccess ==
            if (handlerMethod.getMethodAnnotation(AuthTokeAccess.class) != null) {
                return true;//有AuthTokeAccess注解的方法直接放行 [已解析过token]
            }
            RoleType entityType = UserInfoContext.getUser().getRoleType();
            if (handlerMethod.getBeanType().equals(TeacherController.class)) {
                log.info("在执行TeacherController中的方法");
                if (entityType != RoleType.TEACHER && entityType != RoleType.AUDITOR){
                    throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
                }
            } else if (handlerMethod.getBeanType().equals(AdminController.class)) {
                log.info("在执行AdminController中的方法");
                if (entityType != RoleType.ADMIN){
                    throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
                }
            }else if (handlerMethod.getBeanType().equals(SuperAdminController.class)) {
                log.info("在执行SuperAdminController中的方法");
                if (entityType != RoleType.SUPER_ADMIN){
                    throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_SUPERADMIN);
                }
            }
        }
        return true;
    }

    private void parseTokenAndMakeUser(String token, HttpServletRequest request) {
        //解析token,并将id,oid,cid,createDate放入上下文
        try {
            Claims claims = jwtUtil.parseToken(token);
            String entityType = (String) claims.get("entityType");
            User user = new User(); // 创建一个User实例
//            user.setEntityType(entityType);
            log.debug("================================entityType{}",entityType);
            switch (entityType) {
                case "Teacher":
                    Teacher t_teacher = jwtUtil.parseToken(token, Teacher.class);
                    Long tid = t_teacher.getId();
                    Teacher teacher = teacherMapper.selectById(tid); // 生成token时的包含的信息,除了id,其它可能会变,所以再用id去数据库查询实时数据
                    if (teacher.getIsAuditor()==0){
                        user.setRoleType(RoleType.TEACHER);
                    }else {
                        user.setRoleType(RoleType.AUDITOR);
                    }
                    user.setId(tid);
                    user.setOid(teacher.getOid());
                    user.setCid(teacher.getCid());
                    user.setCreateDate(teacher.getCreateDate());
                    break;
                case "Admin": {
                    user.setRoleType(RoleType.ADMIN);
                    Admin t_admin = jwtUtil.parseToken(token, Admin.class);
                    Long uid = t_admin.getId();
                    Admin admin = adminMapper.selectById(uid); // 生成token时的包含的信息,除了id,其它可能会变,所以再用id去数据库查询实时数据
                    if (admin.getStatus()==0){
                        throw new CustomException(CustomExceptionCodeMsg.USER_IS_DISABLED);
                    }
                    user.setId(uid);
                    user.setCid(admin.getCid());
                    user.setCreateDate(admin.getCreateDate());
                    break;
                }
                case "SuperAdmin": {
                    user.setRoleType(RoleType.SUPER_ADMIN);
                    SuperAdmin admin = jwtUtil.parseToken(token, SuperAdmin.class);
                    user.setId(admin.getId());
                    break;
                }
                default:
                    throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
            }
            UserInfoContext.setUser(user);
            log.debug(request.getRequestURI() + "token解析成功");
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw e;
            }
            log.error(request.getRequestURI() + "token解析失败");
            throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfoContext.remove();//释放线程
    }
}

