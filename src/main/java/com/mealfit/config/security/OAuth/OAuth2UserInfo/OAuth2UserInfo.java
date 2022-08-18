package com.mealfit.config.security.OAuth.OAuth2UserInfo;

import com.mealfit.user.domain.User;

public interface OAuth2UserInfo {

    String getId();

    String getNickname();

    String getEmail();

    String getImageUrl();

    User toEntity();
}
