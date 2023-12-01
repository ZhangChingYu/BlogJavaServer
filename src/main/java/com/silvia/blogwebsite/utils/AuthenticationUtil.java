package com.silvia.blogwebsite.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class AuthenticationUtil {
    // 密鑰
    public static final String SECRET_KEY = "thisissomesamplekeyfortestingthisissomesamplekeyfortestingthisissomesamplekeyfortesting";
    // Token 生成
    public static String generateToken(String username, long expirationMillis){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public static String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
    public static Date getExpirationFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }
    public static boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            // invalid token
            return false;
        }
    }
    // 密碼加密
    public static String hashPassword (String password){
        // 創建Argon2實例
        Argon2 argon2 = Argon2Factory.create();

        try{
            // 使用Argon2進行密碼Hash
            return argon2.hash(10, 65536, 1, password);
        }finally {}
    }
    // 密碼驗證
    public static boolean verifyPassword(String hashedPassword, String inputPassword){
        Argon2 argon2 = Argon2Factory.create();
        try {
            return argon2.verify(hashedPassword, inputPassword);
        } finally{}
    }

/**
    public static void main(String[] args) {
        // 生成Token，有效時間為 1hr
        String token = generateToken("sample", 3600000);
        System.out.println("Generate token: " + token);
        // 驗證Token
        boolean isValid = validateToken(token);
        System.out.println("Is valid token:" + isValid);
        // 從Token中獲取username
        String username = getUsernameFromToken(token);
        System.out.println("Username from token: " + username);
    }
 */
}
