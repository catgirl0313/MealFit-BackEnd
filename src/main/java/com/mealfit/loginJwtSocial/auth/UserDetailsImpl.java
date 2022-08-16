package com.mealfit.loginJwtSocial.auth;

import com.mealfit.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//멤버조회 하려면 디비 조회애야해. 그러면 토큰이 비효율적임.
//기본 필요한 값 유저네임, 페스워드를 토큰에 넣어주면 db 조회하지 않고 token 에서 쓸 수 있으므로 효율성 짱.-스파르타 심화 강의.!
//스프링 시큐리티가 로그인 요청을 가로채서 로그인 진행 완료가 되면userdetails타입의 오브젝트를 스프링 시큐리티의 고유한 세션저장소에 UserDetailsImpl 저장 해준다.
public class UserDetailsImpl implements UserDetails {
    private final User user;  //콤포지션 member -> user로 변경
    //alt+shift+s? 오버라이드해.

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지 리턴턴
   @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 않았는지 리턴. (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴 (true: 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴 (true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정의 권한을 리턴 인가를 해주는 부분 타입이 이상해.
    //계정이 갖고 있는 궈한 목록을 리턴한다.(권한이 여러개 있을 수 있어서 로프를 돌아야 하는데 우리는 한개만..? 놉 관리자 추가 가능성 있음.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collectors = new ArrayList<>();
//        collectors.add(()->{return "ROLE_" + user.getRole();});
//        return collectors;
        return Collections.emptyList();

    }


//        UserRoleEnum role = member.getRole();
//        String authority = role.getAuthority();rnjsgksdjqtdjehehlsl wldnj

//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        //        authorities.add(simpleGrantedAuthority);
//        return Collections.emptyList();
//     return new ArrayList<>();
//

}
