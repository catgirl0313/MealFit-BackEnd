package com.mealfit.config.security.OAuth.OAuth2UserInfo;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> profiles;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.profiles = (Map<String, Object>) attributes.get("profile");
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getNickname() {
        if (profiles == null) {
            return null;
        }

        return (String) profiles.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("account_email");
    }

    @Override
    public String getImageUrl() {
        if (profiles == null) {
            return null;
        }

        return (String) profiles.get("profile_image");
    }


    @Override
    public User toEntity() {
        return User.createSocialUser(getId(), "SOCIAL_LOGIN", getNickname(), getEmail(), ProviderType.KAKAO);
    }
}
