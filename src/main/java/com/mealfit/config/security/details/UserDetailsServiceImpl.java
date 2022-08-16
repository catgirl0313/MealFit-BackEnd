package com.mealfit.config.security.details;

import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;
    //스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데,
    //password 부분 처리는 알아서 함.
    //username 이 DB에 있는지만 확인해주면 됨.
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        return new UserDetailsImpl(user); //시큐리티의 세션의 유저 정보가 저장이 됨.
//        return UserPrincipal.create(user);
    }
}