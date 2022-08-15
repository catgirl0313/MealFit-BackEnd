package com.mealfit.loginJwtSocial.social;

import com.mealfit.common.crypt.CryptoConverter;
import com.mealfit.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

//구글(서드파티)로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
@AllArgsConstructor
@Getter
@Setter
public class GoogleUser {
    public String id;

    private String username;
//    private String password;
//    private String nickname;

    public String email;
    public Boolean verifiedEmail;

    private String profileImage;
//    private double currentWeight;
//    private double goalWeight;
//    private LocalTime startFasting;
//    private LocalTime endFasting;
//    private UserStatus userStatus;

    private String oauth; //소셜 이렇게 로그인 추가 가능?
}






