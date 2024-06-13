package com.todo.api.auth.util;

import com.todo.api.auth.dto.CustomUserInfoDto;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(CustomUserInfoDto userInfoDto) {
        return createToken(userInfoDto, accessTokenExpTime);
    }

    private String createToken(CustomUserInfoDto userInfoDto, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", userInfoDto.id());
        claims.put("account", userInfoDto.account());
        claims.put("role", userInfoDto.role());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(tokenValidity.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("message : {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("message : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("message : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("message : {}", e.getMessage());
        }

        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
