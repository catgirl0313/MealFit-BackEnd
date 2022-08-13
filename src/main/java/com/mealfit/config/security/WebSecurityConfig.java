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

    //시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
    //해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
    //같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formLoginProvider);
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
                .authorizeRequests(request -> request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .anyRequest().permitAll())

//                .and()
//                .formLogin() //인증되지 않은 anyRequest는
//                .loginPage("'auth/loginFoorm") //허용되지 않은 페이지 요청은 무조건 /login/form으로 와와
//                .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당주소로 요청오는 로그인을 가로채서 대신 로그인을 한다.
//                .defaultSuccessUrl("/") // 로그인 정상 요청 완료 후 이동.
//                .failureUrl("/fail") // 실패 시 이쪽 url로 이동. -강의에선 안 만듦.

                .addFilterBefore(new FormLoginFilter(authenticationManager(), secretKey), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userRepository), UsernamePasswordAuthenticationFilter.class);


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
