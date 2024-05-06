package com.aih.utils.jwt;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final long JWT_EXPIRE = 12*60*60*1000L; //12小时 有效期
    private static final String JWT_KEY = "123456";     //密钥

    public  String createToken(Object data,String entityType){
        long currentTime = System.currentTimeMillis();// 当前时间
        long expTime = currentTime+JWT_EXPIRE;  // 过期时间
        String issuer = "小天"; // 签发人名称
        String id = UUID.randomUUID().toString(); // 生成一个随机的唯一标识符

        // 构建jwt
        Claims claims = Jwts.claims().setSubject(JSON.toJSONString(data));
        claims.put("entityType", entityType);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuer(issuer)
                .setIssuedAt(new Date(currentTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecret(JWT_KEY))
                .setExpiration(new Date(expTime));
        return builder.compact();
    }

    private SecretKey encodeSecret(String key){
        byte[] encode = Base64.getEncoder().encode(key.getBytes());
        SecretKeySpec aes = new SecretKeySpec(encode, 0, encode.length, "AES");
        return  aes;
    }

    public Claims parseToken(String token){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    public <T> T parseToken(String token,Class<T> cla){
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return JSON.parseObject(body.getSubject(),cla);
    }
}
