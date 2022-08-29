package com.mealfit.config.security.dto;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
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

    public LoginResponseDto(String accessToken, String refreshToken, UserDetailsImpl userDetails) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userInfoDto = new UserInfoDto(userDetails.getNickName(), userDetails.getProfile(), userDetails.getUser().getUserStatus());
    }
//    public LoginResponseDto(String accessToken, String refreshToken,
//                            UserDetailsImpl userDetails, UserStatus userStatus) {
//        this.tokenBox = new TokenBox(accessToken, refreshToken);
//        this.userInfoDto = new UserInfoDto(userDetails.getUser(), userStatus);
//    }

//    public LoginResponseDto(String accessToken, String refreshToken,
//          String nickname, String profile, UserStatus userStatus) {
//        this.tokenBox = new TokenBox(accessToken, refreshToken);
//        this.userInfoDto = new UserInfoDto(nickname, profile, userStatus);
//    }
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
        private UserStatus userStatus;

        public UserInfoDto(User user) {
            this.nickname = user.getNickname();
            this.profile = user.getProfileImage();
            this.userStatus = user.getUserStatus();

        }

        public UserInfoDto(String nickname, String profile) {
            this.nickname = nickname;
            this.profile = profile;


        }

//
//        public UserInfoDto(User user, UserStatus userStatus) {
//            this.nickname = user.getNickname();
//            this.profile = user.getProfileImage();
//            this.userStatus = userStatus;
//        }
//        public UserInfoDto(User user) {
//            this.nickname = user.getNickname();
//            this.profile = user.getProfileImage();
//        }
//
//        public UserInfoDto(String user, String userStatus) {
//            this.nickname = nickname;
//            this.profile = profile;
//            this.userStatus = userStatus;
//        }
    }

}
