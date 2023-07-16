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
    public void testCreateJwt(){
        Teacher teacher = new Teacher();
        teacher.setUsername("xiaotian");
        teacher.setPwd("123456");
        String  token = jwtUtil.createToken(teacher);
        System.out.println(token);
    }
    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMzZlNTNiNC1iNzgyLTQxYmEtOWRlMS1iZmQ3ZjY3ZjA0MzUiLCJzdWIiOiJ7XCJwd2RcIjpcIjEyMzQ1NlwiLFwidXNlck5hbWVcIjpcInhpYW90aWFuXCJ9IiwiaXNzIjoic3lzdGVtIiwiaWF0IjoxNjg5MjUzMjg1LCJleHAiOjE2ODkyNTUwODV9.UHik-WYOoP93KayP2WnG3uY3Ugkea-gLB3WOLM258dU";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }
    @Test
    public void testParseJwt2(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMzZlNTNiNC1iNzgyLTQxYmEtOWRlMS1iZmQ3ZjY3ZjA0MzUiLCJzdWIiOiJ7XCJwd2RcIjpcIjEyMzQ1NlwiLFwidXNlck5hbWVcIjpcInhpYW90aWFuXCJ9IiwiaXNzIjoic3lzdGVtIiwiaWF0IjoxNjg5MjUzMjg1LCJleHAiOjE2ODkyNTUwODV9.UHik-WYOoP93KayP2WnG3uY3Ugkea-gLB3WOLM258dU";
        Teacher teacher = jwtUtil.parseToken(token,Teacher.class);
        System.out.println(teacher.getUsername());
        System.out.println(teacher);
    }


}
