package com.mealfit.config.security.jwt;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.details.UserDetailsServiceImpl;
import com.mealfit.config.security.jwt.VerifyResult.TokenStatus;
import com.mealfit.config.security.token.JwtAuthenticationToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                                  UserDetailsServiceImpl userDetailsService, RedisTemplate<String, Object> redisTemplate) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain)
          throws IOException, ServletException {

        String accessToken = null;

        try {
            accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        VerifyResult verifyResult = jwtUtils.verifyToken(accessToken);

        if (verifyResult.getTokenStatus() == TokenStatus.AVAILABLE) {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            if (operations.get(accessToken) != null && (boolean) operations.get(accessToken)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "이미 로그아웃 하셨습니다. 다시 로그인 해 주세요");
                return;
            }
            Authentication jwtAuthToken = new JwtAuthenticationToken(verifyResult.getUsername(),
                  null);
            Authentication authentication = getAuthenticationManager().authenticate(jwtAuthToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (verifyResult.getTokenStatus() == TokenStatus.EXPIRED) {
            // TODO: REFRESH_TOKEN Verify 후 재발급 또는 로그아웃 예정
            String refreshToken = extractTokenFromHeader(request, "refresh_token");

            // 반드시 만료된 토큰이 있는 상태에서 refresh_token 이 있어야 함.
            VerifyResult refreshTokenVerifyResult = jwtUtils.verifyToken(refreshToken);
            if (refreshTokenVerifyResult.getTokenStatus() == TokenStatus.AVAILABLE) {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                      refreshTokenVerifyResult.getUsername());
                String reIssueAccessToken = jwtUtils.issueAccessToken(userDetails.getUsername());
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + reIssueAccessToken);

                UsernamePasswordAuthenticationToken resultToken = new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(resultToken);
            } else {
                // Access_Token 유효하지 않은 토큰인 경우
                log.error("유효하지 않은 JWT 발송 감지: " + accessToken);
                throw new IllegalArgumentException("유효하지 않은 JWT 발송 감지: " + accessToken);
            }
        } else {
            log.info("정상적이지 않은 토큰");
            throw new IllegalArgumentException("정상적이지 않은 토큰입니다.");
        }
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request, String tokenType) {
        String headerValue = request.getHeader(tokenType);

        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인 (폼로그인필터에서 발급한 토큰 시크릿키 ->)
        if (headerValue == null || headerValue.isBlank() || !headerValue.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        return headerValue.substring("Bearer ".length());
    }
}
