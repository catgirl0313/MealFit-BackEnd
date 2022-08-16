package com.mealfit.loginJwtSocial.jwtFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.loginJwtSocial.dto.LoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password 전송하면 (psot)
//UsernamePasswordAuthenticationFilter 동작을 함.
//controller에서 지정안해줘도 login으로 읽힘

public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    private String secretKey;

    public FormLoginFilter(AuthenticationManager authenticationManager, String secretKey) {

        super(authenticationManager);
        this.secretKey = secretKey;
    }

    // /login 요청을 하면 로그인 시도를 위해서 함수 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");
        // 1.username, password 받아서
        // 2. 정상인지 로그인 시도를 해보는 것. authenticationManager로 로그인 시도를 하면!!
        // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.
        // PrincipalDetailsService의 loadUserByUsername()함수가 실행된 후 정상이면 authentication이 리턴됨.
        // DB에 있는 username과 password가 일치한다.
        try {
            ObjectMapper om = new ObjectMapper();
            LoginRequestDto loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class); //유저정보 담기
            System.out.println(loginRequestDto); //입력된 값 확인
            System.out.println("==============================================================");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

            System.out.println(authenticationToken);


            Authentication authentication =
                    getAuthenticationManager().authenticate(authenticationToken);

            System.out.println(authentication);

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
    //JWT 토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨.
    //찐토큰 발급.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨: 인증이 완료.");
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal(); //?


        //JWT 토큰 발급
        //RSA방식은 아니고 Hash암호 방식 //jwts : 무슨 문법?
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
                .withClaim("username", userDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(secretKey));

        response.addHeader("Authorization", jwtToken); //헤더에 토큰을 넣어줘.
    }

    //로그인 실패시 예외 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println(failed.getMessage());
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(failed.getMessage());
    }


}
