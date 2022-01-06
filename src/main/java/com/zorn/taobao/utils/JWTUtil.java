package com.zorn.taobao.utils;

import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import com.zorn.taobao.pojo.state.ResponseState;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JWTUtil {
    private static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    //accessToken存活时间
    private static Long ACCESS_TOKEN_EXPIRATION = 3600L * 1000;

    //refreshToken存活时间
    private static Long REFRESH_TOKEN_EXPIRATION = 10 * 24 * 3600L * 1000;

    //jwt的签发者
    private static String JWT_ISS = "Zorn";

    //jwt的所有人
    private static String SUBJECT = "Zorn";

    private static String secret = "taobao2021";

    public static String getToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user);
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return Jwts.builder().setIssuer(JWT_ISS)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .setHeader(header)
                .setSubject(SUBJECT)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    public static String getRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", user);
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return Jwts.builder().setIssuer(JWT_ISS)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .setHeader(header)
                .setSubject(SUBJECT)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    public static ResponseData checkToken(String token) {
        token = token.substring(7, token.length());
        if (token == null) {
            return new ResponseData(ResponseState.TOKEN_IS_ERROR.getMessage(), ResponseState.TOKEN_NOT_PROVIDE.getValue());
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return new ResponseData(ResponseState.REFRESH_TOKEN_IS_EXPIRED.getMessage(), ResponseState.REFRESH_TOKEN_IS_EXPIRED.getValue());
        } catch (Exception e) {
            return new ResponseData(ResponseState.REFRESH_TOKEN_IS_EXPIRED.getMessage(), ResponseState.REFRESH_TOKEN_IS_EXPIRED.getValue());
        }
        return new ResponseData(ResponseState.SUCCESS.getMessage(), ResponseState.SUCCESS.getValue());
    }

    public static User getUser(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Map<String, Object> userMap = (Map<String, Object>) claims.get("user");
        int id = (int) userMap.get("id");
        String username = (String) userMap.get("username");
        String phone = (String) userMap.get("phone");
        User user = new User(id, username, phone);
        return user;
    }

    public static User getUser(String token, HttpServletRequest request) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Map<String, Object> userMap = (Map<String, Object>) claims.get("user");
        int id = (int) userMap.get("id");
        String username = (String) userMap.get("username");
        String phone = (String) userMap.get("phone");
        User user = new User(id, username, phone);
        request.setAttribute("user",user);
        return user;
    }
}
