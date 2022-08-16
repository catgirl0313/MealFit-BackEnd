package com.mealfit.config.security.jwt;

import com.mealfit.config.security.jwt.VerifyResult.TokenStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 전용 유틸리티 클래스
 *    - JWT 생성 및 해독이 너무 많은 클래스 퍼져 있었다.
 *    - 이를 한 클래스로 기능을 모아서 쓸 수 있게 리팩토링
 */
@Component
public class JwtUtils {

    private final String SECRET_KEY;
    private final String ACCESS_EXPIRED_TIME;
    private final String REFRESH_EXPIRED_TIME;

    public JwtUtils(
          @Value("${jwt.secret-key}") String SECRET_KEY,
          @Value("${jwt.access-expired-time}") String ACCESS_EXPIRED_TIME,
          @Value("${jwt.refresh-expired-time}") String REFRESH_EXPIRED_TIME) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
    }

    public String issueAccessToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(ACCESS_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    // TODO 리프레시 토큰 생성 예정
    public String issueRefreshToken(String username) {
//        RefreshTokenRedis refreshToken = createRefreshToken(username);
//        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
//        operations.set(username, refreshToken.getToken());
        return createRefreshToken(username);
    }

    private String createRefreshToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(REFRESH_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    public VerifyResult verifyToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                  .getBody();
            return new VerifyResult(claims.getSubject(), TokenStatus.AVAILABLE);
        } catch (SecurityException e) {
            return new VerifyResult(null, TokenStatus.DENIED);
        }  catch (ExpiredJwtException e) {
            return new VerifyResult(null, TokenStatus.EXPIRED);
        } catch (RuntimeException e) {
            return new VerifyResult(null, TokenStatus.DENIED);
        }
    }
}