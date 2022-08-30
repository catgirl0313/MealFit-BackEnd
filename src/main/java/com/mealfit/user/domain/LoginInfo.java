package com.mealfit.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LoginInfo {

    @Column(nullable = false, unique = true,  updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder
    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
