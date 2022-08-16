package com.mealfit.loginJwtSocial.jwtFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.loginJwtSocial.auth.UserDetailsServiceImpl;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//시큐리티가 filter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.!!!!!!
//만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐.
//500에러 . 통과해야 막힘 풀림. 확실함 슬기 피셜.
//권한이 있느 페이지를 만나면 헤더에있는 토큰을 꺼낸다.
//궈한 필요한 인증이나 권한이 필요한 주소 요청이 오면,
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

        //해더에서 추출
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: "+ jwtHeader); //토큰값 확인

        //header가 있는지 확인
        if(jwtHeader == null) {
            chain.doFilter(request, response);
            return;
        }

        //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인 (폼로그인필터에서 발급한 토큰 시크릿키 ->)
        String jwtToken = request.getHeader("Authorization");

        String username =
                JWT.require(Algorithm.HMAC512("A134qwelkjfcmn341123")).build().verify(jwtToken).getClaim("username").asString();

        //서명이 정상적으로 됨.
        if(username != null) {
            User userEntity = userRepository.findByUsername(username).orElseThrow(
                    ()-> new IllegalArgumentException("username이 없습니다.")
            );

//            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);

            //Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

            //홀더에 검증이 완료된 정보 값 넣어준다. -> 이제 controller 에서 @AuthenticationPrincipal UserDetailsImpl userDetails 로 정보를 꺼낼 수 있다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //세션을 사용하지 않으므로 컨텍스트홀더에 임시 저장안해주면 토큰 인증받아도 유저정보를 가져올수없음. 아주중요.
            //authentication 인증 되면 세션처럼 임시 저장, 유저정보를 가져다 쓸 수 있음. 이슬기짱.구자슬기짱.

            //조금만 더 공부하면~? 숙제까지 내주심 짱짱맨. 유저디테일즈 정보를 토큰에 넣어 디비 조회 안할 수 있음.

            chain.doFilter(request, response); //만약 return 주면 return 에서 함수가 끝나는데 do로 다음 filter로 넘겨 작동시키기 위함.
        }

    }
}
