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
    private UserInfoDto userInfoDto;

    public LoginResponseDto(String accessToken, String refreshToken,
          UserDetailsImpl userDetails) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userInfoDto = new UserInfoDto(userDetails.getUser());
    }

    public LoginResponseDto(String accessToken, String refreshToken,
          String nickname, String profile) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userInfoDto = new UserInfoDto(nickname, profile);
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
    static class UserInfoDto {

        private String nickname;
        private String profile;

        public UserInfoDto(User user) {
            this.nickname = nickname;
            this.profile = profile;
        }
    }

}
