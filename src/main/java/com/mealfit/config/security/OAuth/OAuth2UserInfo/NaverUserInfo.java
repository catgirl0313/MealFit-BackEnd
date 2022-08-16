package com.mealfit.config.security.OAuth.OAuth2UserInfo;

import com.mealfit.config.security.OAuth.ProviderType;
import com.mealfit.user.domain.User;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> profiles;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.profiles = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        if (profiles == null) {
            return null;
        }

        return (String) profiles.get("id");
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
        if (profiles == null) {
            return null;
        }

        return (String) profiles.get("email");
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
        return User.createSocialUser(getId(), "SOCIAL_LOGIN", getNickname(), getEmail(), ProviderType.NAVER);
    }
}
