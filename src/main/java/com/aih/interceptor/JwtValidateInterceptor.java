package com.aih.interceptor;

import com.aih.common.R;
import com.aih.common.utils.JwtUtil;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import kotlin.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.PrimitiveIterator;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token  = request.getHeader("token");
        log.debug(request.getRequestURI() + "需要验证： " + token);
        if(token != null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败，禁止访问");
        response.setContentType("application/json;charset=utf-8");
        R<Object> fail = R.error(103, "jwt无效，请重新登录");
        response.getWriter().write(JSON.toJSONString(fail));
        return false;
    }
}
