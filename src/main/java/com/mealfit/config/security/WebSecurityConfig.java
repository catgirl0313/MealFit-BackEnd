package com.mealfit.config.security;

import com.mealfit.loginJwtSocial.auth.UserDetailsServiceImpl;
import com.mealfit.loginJwtSocial.jwtFilter.FormLoginFilter;
import com.mealfit.loginJwtSocial.jwtFilter.FormLoginProvider;
import com.mealfit.loginJwtSocial.jwtFilter.JwtAuthorizationFilter;
import com.mealfit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsUtils;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final FormLoginProvider formLoginProvider;
    private final UserRepository userRepository;

    @Value("${jwt.secret-key}") // spring 에서 di 할 때 작동됨.
    private String secretKey;

    @Bean   // 비밀번호 암호화
    public PasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override // Bean 에 등록
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean //Q.물어보기기이건  있는거죠? 자현님 강의에만 있음.
//    public FormLoginProvider formLoginProvider(){
//        return new FormLoginProvider(userDetailsService, encodePassword());
//    }

    //시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
    //해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
    //같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(formLoginProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(encodePassword()); //userdetailsservice. null자리 오브젝트한테 알려줘야해. 만들러가기~
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.cors(withDefaults());

        // h2-console 설정
        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

        // jwt 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .formLogin().disable() //JWT 토큰 방식으로 인증 및 인가 처리 하기 때문에 Security 에서 기본적으로 제공하는 FORM 로그인, Logout 등 옵션을 비활성화 하고 자체적으로 Custom
                .logout().disable()
                .httpBasic().disable()
                .authorizeRequests()  //request -> request
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .antmachers("user/login/auth").permitAll()
                .antMatchers("/user/login/auth").authenticated();
//            .and()
//                .oauth2Login()
//                .defaultSuccessUrl("/login-success")
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService);	// oauth2 로그인에 성공하면, 유저 데이터를 가지고 우리가 생성한
//        // customOAuth2UserService에서 처리를 하겠다. 그리고 "/login-success"로 이동하라.

        http.addFilterBefore(new FormLoginFilter(authenticationManager(), secretKey), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, userRepository), UsernamePasswordAuthenticationFilter.class);


    }

    @Override // ignore check swagger resource  // 합쳤는데, 바꿔도 되나요?
    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/v2/api-docs", "/swagger-resources/**",
                        "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger/**");
    }
}

// 정적 자원에 대해서는 Security 설정을 적용하지 않음.
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

//    @Override // ignore check swagger resource  // 시도해봤는데 webseurity web 빨간줄뜸.
//    public void configure(WebSecurity web) {
//        web.ignoring()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//                .antMatchers("/v2/api-docs", "/swagger-resources/**",
//                        "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger/**");
//    }
