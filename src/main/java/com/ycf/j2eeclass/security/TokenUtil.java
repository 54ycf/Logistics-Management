package com.ycf.j2eeclass.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TokenUtil {

    private static final long expiration = 1000*60*60*8; // 每个token有效期8h
    private static final String mySignature = "+90h26-n$8o388*9?"; //自己的签名

    public static String createJwtStr(String username, String id, String role){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .claim("username",username)
                .claim("id",id)
                .claim("role",role)
                .setSubject("logisticsAdmin")
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256,mySignature)
                .compact();
        System.out.println(jwtToken);
        return jwtToken;
    }

    public static boolean checkToken(String jwtToken){
        System.out.println(jwtToken);
        if (jwtToken == null) return false;
        try{
            Jwts.parser().setSigningKey(mySignature).parseClaimsJws(jwtToken);
        }catch (Exception e){
            throw e;
        }
        return true;
    }

    public static String parseInfo(String jwtToken, String key){
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(mySignature).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return claims.get(key).toString();
    }

}
