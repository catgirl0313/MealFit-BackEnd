package com.mealfit.user.domain;

public enum OAuthType {

    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    NONE("none");

    private String code;

    OAuthType(String code) {
        this.code = code;
    }
}
