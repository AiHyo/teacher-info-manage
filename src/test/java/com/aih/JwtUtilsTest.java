package com.aih;

import com.aih.utils.jwt.JwtUtil;
import com.aih.entity.Teacher;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void createJwt(){
        Teacher teacher = new Teacher();
        teacher.setUsername("xiaotian");
        teacher.setPassword("123456");
        String  token = jwtUtil.createToken(teacher,"Teacher");
        System.out.println(token);
    }
    @Test
    public void parseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJwd2RcIjpcIjEyMzQ1NlwiLFwidXNlcm5hbWVcIjpcInhpYW90aWFuXCJ9IiwiZW50aXR5VHlwZSI6IlRlYWNoZXIiLCJqdGkiOiI5ZGE5MGQwZi0xZDJmLTQyMmEtYTBjZi03Mjc3YTMyOWFhM2EiLCJpc3MiOiLlsI_lpKkiLCJpYXQiOjE2OTEyMjAzODQsImV4cCI6MTY5MTIyMjE4NH0.HJ6S5lF4ivZ2jENKtsPuyQc2LMpr3VlkoGPKM7K_QJU";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
        System.out.println(claims.getSubject());
        System.out.println((String) claims.get("entityType"));
        //字符串转成json


    }
    @Test
    public void parseJwt_2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJwd2RcIjpcIjEyMzQ1NlwiLFwidXNlcm5hbWVcIjpcInhpYW90aWFuXCJ9IiwiZW50aXR5VHlwZSI6IlRlYWNoZXIiLCJqdGkiOiI5ZGE5MGQwZi0xZDJmLTQyMmEtYTBjZi03Mjc3YTMyOWFhM2EiLCJpc3MiOiLlsI_lpKkiLCJpYXQiOjE2OTEyMjAzODQsImV4cCI6MTY5MTIyMjE4NH0.HJ6S5lF4ivZ2jENKtsPuyQc2LMpr3VlkoGPKM7K_QJU";
        Teacher teacher = jwtUtil.parseToken(token,Teacher.class);
        System.out.println(teacher.getUsername());
        System.out.println(teacher);
    }




}
