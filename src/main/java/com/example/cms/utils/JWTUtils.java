package com.example.cms.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    static final String SECRET = "ThisIsASecret";
    static public String generateToken(String userName) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("username", userName);
        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000_000L)) // 1000 hour
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return "Bearer "+jwt; // 这个Bearer 后端 前端 加都可以
    }

    static public void validateToken(String token) {
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
        }catch (Exception e) {
            throw new IllegalStateException("Invalid Token. "+e.getMessage());
        }
    }
}
