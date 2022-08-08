package com.mealfit.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic().disable();
        http.cors(withDefaults());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
              .authorizeRequests(request -> request
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .anyRequest().permitAll());
    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
              "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/swagger/**");
    }
}
