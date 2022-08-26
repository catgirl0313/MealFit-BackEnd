package com.mealfit.config.security.formlogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.dto.LoginRequestDto;
import com.mealfit.config.security.dto.LoginResponseDto;
import com.mealfit.config.security.jwt.JwtUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password 전송하면 (psot)
//UsernamePasswordAuthenticationFilter 동작을 함.
//controller에서 지정안해줘도 login으로 읽힘
@Slf4j
//@CrossOrigin
public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
//    private ObjectMapper objectMapper = new ObjectMapper(); //final로 만드는게 좋은건가? //밖에 써도 괜찮아?

    public FormLoginFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {

        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    // /login 요청을 하면 로그인 시도를 위해서 함수 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter : 로그인 시도중");
//        ObjectMapper objectMapper = new ObjectMapper();
        // 1.username, password 받아서
        // 2. 정상인지 로그인 시도를 해보는 것. authenticationManager로 로그인 시도를 하면!!
        // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.
        // PrincipalDetailsService의 loadUserByUsername()함수가 실행된 후 정상이면 authentication이 리턴됨.
        // DB에 있는 username과 password가 일치한다.
//        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class); //유저정보 담기
            log.info("loginRequestDto = {}", loginRequestDto); //입력된 값 확인
            log.info("==============================================================");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

            log.info("authenticationToken = {}", authenticationToken);

            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
        }
    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
    //JWT 토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨.
    //찐토큰 발급.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 실행됨: FormLoginProvider 인증 완료.");
        ObjectMapper objectMapper = new ObjectMapper();

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String accessToken = jwtUtils.issueAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtils.issueRefreshToken(userDetails.getUsername());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken, userDetails);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
    }

    //로그인 실패시 예외 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(failed.getMessage());
    }


}
