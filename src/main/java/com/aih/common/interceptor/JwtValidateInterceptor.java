package com.aih.common.interceptor;

import com.aih.utils.UserInfoContext;
import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.Teacher;
import com.aih.utils.CustomException.CustomException;
import com.aih.utils.CustomException.CustomExceptionCodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
            return true;
        }
        String token  = request.getHeader("token");
//        log.debug(request.getRequestURI() + "需要验证： " + token);
        if(token != null){
            try {
                //解析token,并将tid,cid放入上下文
                Teacher loginTeacher = jwtUtil.parseToken(token, Teacher.class);
                UserInfoContext.setTeacher(loginTeacher);
                log.debug(request.getRequestURI() + "token解析成功");
                return true;
            } catch (Exception e) {
                log.debug(request.getRequestURI() + "token解析失败");
                log.debug(e.getMessage());
            }
        }
//        response.setContentType("application/json;charset=utf-8");
//        R<Object> fail = R.error(103, "token解析失败,请重新登录");
//        response.getWriter().write(JSON.toJSONString(fail));
        throw new CustomException(CustomExceptionCodeMsg.TOKEN_INVALID);
    }
}
