package com.mealfit.loginJwtSocial.social;

import com.mealfit.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class KakaoUser {

    public String id;

    private String username;
    private String password;
    private String nickname;

    public String email;
    public Boolean verifiedEmail;

    private String profileImage;
    private double currentWeight;
    private double goalWeight;
    private LocalTime startFasting;
    private LocalTime endFasting;
    private UserStatus userStatus;

    private String oauth; //소셜 이렇게 로그인 추가 가능?
}
