package com.mealfit.config.security.OAuth.OAuth2UserInfo;

import com.mealfit.user.domain.LoginInfo;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserProfile;
import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }


    @Override
    public User toEntity() {
        return User.createSocialUser(
              new LoginInfo(getId(), "SOCIAL_LOGIN"),
              new UserProfile(getNickname(), getEmail(), getImageUrl()),
              ProviderType.GOOGLE);
    }
}
