package com.mealfit.user.domain;

public enum UserStatus {
    NOT_VALID,              // 이메일 미인증 플래그
    FIRST_SOCIAL_LOGIN,     // 최초 정보 설정 + 앱 튜토리얼을 위한 플래그
    FIRST_LOGIN,            // 앱 튜토리얼을 위한 플래그
    NORMAL,                 // 최초 로그인 이후 두번째 로그인 부터 정상적인 유저의 플래그
    DELETE                  // 회원 탈퇴 이후 1년 정보 수집을 위한 플래그 -> 1년이 되면 삭제(Spring Batch)
}
