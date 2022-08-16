package com.mealfit.config.security.dto;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private TokenBox tokenBox;
    private UserInfo userInfo;

    public LoginResponseDto(String accessToken, String refreshToken,
          UserDetailsImpl userDetails) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userInfo = new UserInfo(userDetails.getUser());
    }

    public LoginResponseDto(String accessToken, String refreshToken,
          String nickname, String profile) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userInfo = new UserInfo(nickname, profile);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class TokenBox {
        private String accessToken;
        private String refreshToken;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserInfo {

        private String nickname;
        private String profile;

        public UserInfo(User user) {
            this.nickname = nickname;
            this.profile = profile;
        }
    }

}
