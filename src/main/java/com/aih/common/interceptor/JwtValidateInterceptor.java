package com.aih.common.interceptor;

import com.aih.controller.AdminController;
import com.aih.controller.SuperAdminController;
import com.aih.controller.TeacherController;
import com.aih.entity.Admin;
import com.aih.entity.SuperAdmin;
import com.aih.utils.AuthAccess;
import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.Teacher;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import com.aih.utils.vo.R;
import com.aih.utils.vo.User;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().contains("login")){
            return true;//登录请求直接放行
        }
        if (handler instanceof HandlerMethod) {
            AuthAccess methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if(methodAnnotation != null){
                return true;//有该注解的方法直接放行
            }
        }

        String token  = request.getHeader("token");
//        log.debug(request.getRequestURI() + "需要验证： " + token);
        if(token == null){
            throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
            /*response.setContentType("application/json;charset=utf-8");
            R<Object> fail = R.error(103, "token解析失败,请重新登录");
            response.getWriter().write(JSON.toJSONString(fail));*/
        }

        //解析token,并将id,oid,cid,createDate放入上下文
        try {
            Claims claims = jwtUtil.parseToken(token);
            String entityType = (String) claims.get("entityType");
            User user = new User(); // 创建一个User实例
//            user.setEntityType(entityType);
            switch (entityType) {
                case "Teacher":
                    Teacher teacher = jwtUtil.parseToken(token, Teacher.class);
                    user.setId(teacher.getId());
                    user.setOid(teacher.getOid());
                    user.setCid(teacher.getCid());
                    user.setCreateDate(teacher.getCreateDate());
                    break;
                case "Admin": {
                    Admin admin = jwtUtil.parseToken(token, Admin.class);
                    user.setId(admin.getId());
                    user.setCid(admin.getCid());
                    user.setCreateDate(admin.getCreateDate());
                    break;
                }
                case "SuperAdmin": {
                    SuperAdmin admin = jwtUtil.parseToken(token, SuperAdmin.class);
                    user.setId(admin.getId());
                    break;
                }
                default:
                    throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
            }
            if (handler instanceof HandlerMethod) {//判断是否是HandlerMethod
                HandlerMethod handlerMethod = (HandlerMethod) handler;//强转为HandlerMethod
                if (handlerMethod.getBeanType().equals(TeacherController.class)) {
                    log.info("在执行TeacherController中的方法");
                    if (!entityType.equals("Teacher")) {
                        throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_TEACHER);
                    }
                } else if (handlerMethod.getBeanType().equals(AdminController.class)) {
                    log.info("在执行AdminController中的方法");
                    if (!entityType.equals("Admin")) {
                        throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_ADMIN);
                    }
                }else if (handlerMethod.getBeanType().equals(SuperAdminController.class)) {
                    log.info("在执行SuperAdminController中的方法");
                    if (!entityType.equals("SuperAdmin")) {
                        throw new CustomException(CustomExceptionCodeMsg.USER_IS_NOT_SUPERADMIN);
                    }
                }
            }
            UserInfoContext.setUser(user);
            log.debug(request.getRequestURI() + "token解析成功");
            return true;
        } catch (Exception e) {
            if (e instanceof CustomException) {
                throw e;
            }
            log.error(request.getRequestURI() + "token解析失败");
            throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
        }
    }
}
