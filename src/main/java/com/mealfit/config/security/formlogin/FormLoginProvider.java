package com.mealfit.config.security.formlogin;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.config.security.details.UserDetailsServiceImpl;
import com.mealfit.exception.authentication.BadLoginInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FormLoginProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        String username = (String) authentication.getPrincipal();  // 첫번째값 꺼내기 - 물어보기
        String password = (String) authentication.getCredentials(); // 두번째값 꺼내기 - 물어보기, 세번째는 있나요 ?

        UserDetailsImpl userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (userDetails.getUser().isNotValid()) {
            throw new BadLoginInfoException("이메일 인증을 받지 않았습니다. 이메일 인증을 해주세요!");
        }

        if (userDetails.getUser().isSocialUser()) {
            throw new BadLoginInfoException("잘못된 로그인 방식입니다.");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null);
        } else {
            throw new BadCredentialsException("잘못된 로그인 정보입니다."); //인증 실패
        }
    }


    @Override
    //프로바이더를 탈려면 이게 있어야해.  폼로그인필터에서 authentication 만들면, authentication 값이 만들어지면 provider를 탄다.
    public boolean supports(Class<?> authentication) {

        // provider의 supports 값이 false를 리턴하면, provider의 authenticate 메소드가 호출되지 않는다.
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
