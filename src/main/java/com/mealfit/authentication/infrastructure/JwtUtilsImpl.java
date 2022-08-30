package com.mealfit.authentication.infrastructure;

import com.mealfit.authentication.application.JwtUtils;
import com.mealfit.authentication.domain.JwtTokenVerifyResult;
import com.mealfit.authentication.domain.JwtTokenVerifyResult.TokenStatus;
import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilsImpl implements JwtUtils {

    private final String SECRET_KEY;
    private final String ACCESS_EXPIRED_TIME;
    private final String REFRESH_EXPIRED_TIME;


    public JwtUtilsImpl(
          @Value("${jwt.secret-key}") String SECRET_KEY,
          @Value("${jwt.access-expired-time}") String ACCESS_EXPIRED_TIME,
          @Value("${jwt.refresh-expired-time}") String REFRESH_EXPIRED_TIME) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
    }

    public JwtToken issueAccessJwtToken(String username) {
        return new JwtToken(username, createAccessToken(username),
              Long.parseLong(ACCESS_EXPIRED_TIME), JwtTokenType.ACCESS);
    }

    private String createAccessToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(ACCESS_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    public JwtToken issueRefreshJwtToken(String username) {
        return new JwtToken(username, createRefreshToken(username),
              Long.parseLong(REFRESH_EXPIRED_TIME), JwtTokenType.REFRESH);
    }

    private String createRefreshToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(REFRESH_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    public JwtTokenVerifyResult verifyToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                  .getBody();
            return new JwtTokenVerifyResult(claims.getSubject(), TokenStatus.AVAILABLE);
        } catch (SecurityException e) {
            return new JwtTokenVerifyResult(null, TokenStatus.DENIED);
        } catch (ExpiredJwtException e) {
            return new JwtTokenVerifyResult(null, TokenStatus.EXPIRED);
        } catch (RuntimeException e) {
            return new JwtTokenVerifyResult(null, TokenStatus.DENIED);
        }
    }

    @Override
    public JwtToken issueBlackListToken(String token) {
        return new JwtToken(token, "true", Long.parseLong(ACCESS_EXPIRED_TIME),
              JwtTokenType.BLACKLIST);
    }
}
