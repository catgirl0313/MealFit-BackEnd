package com.mealfit.config.security.provider;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.details.UserDetailsServiceImpl;
import com.mealfit.config.security.token.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthorizationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {

        String username = (String) authentication.getPrincipal();

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);

        return new JwtAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
