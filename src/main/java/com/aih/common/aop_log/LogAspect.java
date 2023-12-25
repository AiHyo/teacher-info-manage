package com.aih.common.aop_log;


import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.aih.utils.UserInfoContext;
import com.aih.utils.http.HttpContextUtils;
import com.aih.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.aih.common.aop_log.LogAnnotation)")
    public void logPointCut() {
    }


    //环绕通知
    @Around("logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executeTime = System.currentTimeMillis() - beginTime;
        recordLog(joinPoint, executeTime);
        return result;
    }

    private void recordLog(ProceedingJoinPoint joinPoint, long executeTime) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("====================log start====================");
        log.info("module:{}", logAnnotation.module());
        log.info("operation:{}", logAnnotation.operator());
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}", className + "." + methodName + "()");
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", UserInfoContext.getUser().getId());
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], args[i]);
        }
        log.info("params:{}", JSONUtil.toJsonStr(map));
        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String clientIP = ServletUtil.getClientIP(request);
        log.info("真实ip:{}", IpUtils.getIpAddr(request));
        log.info("ServletUtil.getClientIP:{}", clientIP);
        log.info("IpAddress:{}", IpUtils.getAddressByIp(clientIP));

        log.info("executeTime:{}ms", executeTime);
        log.info("====================log end====================");
    }


}
