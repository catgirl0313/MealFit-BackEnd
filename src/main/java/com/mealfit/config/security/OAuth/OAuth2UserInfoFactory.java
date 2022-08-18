package com.mealfit.config.security.OAuth;

import com.mealfit.config.security.OAuth.OAuth2UserInfo.GoogleUserInfo;
import com.mealfit.config.security.OAuth.OAuth2UserInfo.KakaoUserInfo;
import com.mealfit.config.security.OAuth.OAuth2UserInfo.NaverUserInfo;
import com.mealfit.config.security.OAuth.OAuth2UserInfo.OAuth2UserInfo;
import com.mealfit.user.domain.ProviderType;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleUserInfo(attributes);
            case NAVER: return new NaverUserInfo(attributes);
            case KAKAO: return new KakaoUserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}