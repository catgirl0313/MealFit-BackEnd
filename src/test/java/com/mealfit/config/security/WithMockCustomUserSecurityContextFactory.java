package com.mealfit.config.security;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.token.JwtAuthenticationToken;
import com.mealfit.user.domain.FastingTime;
import com.mealfit.user.domain.LoginInfo;
import com.mealfit.user.domain.Nutrition;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserProfile;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.domain.UserStatusInfo;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

public class WithMockCustomUserSecurityContextFactory implements
      WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : customUser.authorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        if (grantedAuthorities.isEmpty()) {
            for (String role : customUser.roles()) {
                Assert.isTrue(!role.startsWith("ROLE_"), () -> "roles cannot start with ROLE_ Got " + role);
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        else if (!(customUser.roles().length == 1 && "USER".equals(customUser.roles()[0]))) {
            throw new IllegalStateException("You cannot define roles attribute " + Arrays.asList(customUser.roles())
                  + " with authorities attribute " + Arrays.asList(customUser.authorities()));
        }

        UserDetailsImpl userDetails = UserDetailsImpl.create(new User(1L,
              new LoginInfo("username", null),
              new UserProfile("nickname", "test@gmail.com", "https://github.com/profileImg"),
              70.0,
              new FastingTime(LocalTime.of(1, 0), LocalTime.of(10, 0)),
              new Nutrition(2500, 300, 180, 50),
              new UserStatusInfo(UserStatus.NORMAL, ProviderType.LOCAL)));

        final JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails,
              null, grantedAuthorities);

        context.setAuthentication(jwtAuthenticationToken);

        return context;
    }
}
