package com.mealfit.config.security.filter;

import com.mealfit.common.error.ErrorCode;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.details.UserDetailsServiceImpl;
import com.mealfit.config.security.exception.DeniedJwtException;
import com.mealfit.config.security.jwt.JwtUtils;
import com.mealfit.config.security.jwt.VerifyResult;
import com.mealfit.config.security.jwt.VerifyResult.TokenStatus;
import com.mealfit.config.security.token.JwtAuthenticationToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


//시큐리티가 filter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.!!!!!!
//만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐.?
//500에러 . 통과해야 막힘 풀림.
//권한이 필요한 페이지를 만나면 헤더에있는 토큰을 꺼낸다.

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain)
          throws IOException, ServletException {

        log.info("=== JWT AUTH FILTER ===");
//        String accessToken = null;
        String accessHeader = request.getHeader(HttpHeaders.AUTHORIZATION);//
//
        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) { //
            chain.doFilter(request, response);
            return;
        }
//        String accessToken = accessHeader.substring("Bearer ".length()); //
//        String accessToken = null;
//        //해더에서 추출
//        try {
//             accessToken = extractTokenFromHeader(request, HttpHeaders.AUTHORIZATION);//,
//        } catch (IllegalArgumentException e) {
//            chain.doFilter(request, response);
//            return;
//        }
        String accessToken = accessHeader.substring("Bearer ".length()); //

        VerifyResult verifyResult = jwtUtils.verifyToken(accessToken);

        if (verifyResult.getTokenStatus() == TokenStatus.AVAILABLE) {
            //Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication jwtAuthToken = new JwtAuthenticationToken(verifyResult.getUsername(), null);

            // Provider 들에게 처리하라고 던져주기
            Authentication authentication = getAuthenticationManager().authenticate(jwtAuthToken);

            // 홀더에 검증이 완료된 정보 값 넣어준다. -> 이제 controller 에서
            // @AuthenticationPrincipal UserDetailsImpl userDetails 로 정보를 꺼낼 수 있다.
            // 세션을 사용하지 않으므로 컨텍스트홀더에 임시 저장안해주면 토큰 인증받아도 유저정보를 가져올수없음. 아주중요.
            SecurityContextHolder.getContext().setAuthentication(authentication);


            // authentication 인증 되면 세션처럼 임시 저장, 유저정보를 가져다 쓸 수 있음. 이슬기짱.구자슬기짱.

            // 조금만 더 공부하면~? 숙제까지 내주심 짱짱맨. 유저디테일즈 정보를 토큰에 넣어 디비 조회 안할 수 있음.

            //만약 return 주면 return 에서 함수가 끝나는데 do로 다음 filter로 넘겨 작동시키기 위함.
            chain.doFilter(request, response);
        } else if (verifyResult.getTokenStatus() == TokenStatus.EXPIRED) {
            // TODO: REFRESH_TOKEN Verify 후 재발급 또는 로그아웃 예정
            String refreshToken = extractTokenFromHeader(request);

            // 반드시 만료된 토큰이 있는 상태에서 refresh_token 이 있어야 함.
            VerifyResult refreshTokenVerifyResult = jwtUtils.verifyToken(refreshToken);
            if (refreshTokenVerifyResult.getTokenStatus() == TokenStatus.AVAILABLE) {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                        refreshTokenVerifyResult.getUsername());
                String reIssueAccessToken = jwtUtils.issueAccessToken(userDetails.getUsername());
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "
                        + reIssueAccessToken);

                UsernamePasswordAuthenticationToken resultToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(resultToken);
            } else {
                // Access_Token 유효하지 않은 토큰인 경우
                logger.error("유효하지 않은 JWT 발송 감지: " + accessToken);
                throw new DeniedJwtException("유효하지 않은 JWT 발송 감지: " + accessToken);
            }
        } else {
            log.info("정상적이지 않은 토큰");
            throw new DeniedJwtException("정상적이지 않은 토큰입니다.");
//            request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
        }
        chain.doFilter(request, response);
    }
    private String extractTokenFromHeader(HttpServletRequest request) { //, String tokenType
        log.info("request : {}", request);

        String headerValue = request.getHeader("refreshToken"); //tokenType
        log.info("headerValue : {}", headerValue);

        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인 (폼로그인필터에서 발급한 토큰 시크릿키 ->)
        if (headerValue == null || headerValue.isBlank() || !headerValue.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }
        return headerValue.substring("Bearer ".length());
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
          HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(failed.getMessage());
    }
}
