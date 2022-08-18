package com.mealfit.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.mealfit.config.security.OAuth.CustomOAuth2UserService;
import com.mealfit.config.security.OAuth.handler.OAuth2SuccessHandler;
import com.mealfit.config.security.details.UserDetailsServiceImpl;
import com.mealfit.config.security.filter.FormLoginFilter;
import com.mealfit.config.security.filter.JwtAuthorizationFilter;
import com.mealfit.config.security.jwt.JwtUtils;
import com.mealfit.config.security.provider.FormLoginProvider;
import com.mealfit.config.security.provider.JwtAuthorizationProvider;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsServiceImpl userDetailsService;
    private final FormLoginProvider formLoginProvider;
    private final JwtAuthorizationProvider jwtAuthProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtUtils jwtUtils;

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
        auth.authenticationProvider(jwtAuthProvider);
        auth.userDetailsService(userDetailsService);
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

        http.authorizeRequests()
              .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
              .antMatchers("/",
                    "/user/signup",
                    "/user/username", "/user/email", "/user/email", "/user/nickname",
                    "/user/validate",
                    "/find/**").permitAll()
              .anyRequest().authenticated();

        http.addFilterBefore(new FormLoginFilter(authenticationManager(), jwtUtils),
                    UsernamePasswordAuthenticationFilter.class)
              .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), jwtUtils),
                    UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login()
              .successHandler(oAuth2SuccessHandler)
              .userInfoEndpoint()
              .userService(customOAuth2UserService);
    }

    @Override // ignore check swagger resource  // 합쳤는데, 바꿔도 되나요?
    public void configure(WebSecurity web) {
        web.ignoring()
              .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
              .antMatchers("/v2/api-docs", "/swagger-resources/**",
                    "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger/**");
    }
}
