package com.mealfit.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponseDto implements Serializable {

    private Long userId;

    @JsonProperty("userProfile")
    private UserProfileResponseDto userProfile;

    @JsonProperty("fastingInfo")
    private UserFastingResponseDto fastingInfo;

    public UserInfoResponseDto(User user) {
        this.userId = user.getId();
        this.userProfile = new UserProfileResponseDto(user);
        this.fastingInfo = new UserFastingResponseDto(user);
    }

    public UserInfoResponseDto(Long userId, String username, String nickname, String profileImage,
          double goalWeight, UserStatus userStatus, LocalTime startFasting, LocalTime endFasting) {
        this.userId = userId;
        this.userProfile = new UserProfileResponseDto(username, nickname, profileImage, goalWeight, userStatus);
        this.fastingInfo = new UserFastingResponseDto(startFasting, endFasting);
    }

    @Data
    public static class UserProfileResponseDto {

        private String username;
        private String nickname;
        private String profileImage;
        private double goalWeight;
        private UserStatus userStatus;

        public UserProfileResponseDto(User user) {
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
            this.goalWeight = user.getGoalWeight();
            this.userStatus = user.getUserStatus();
        }

        public UserProfileResponseDto(String username, String nickname, String profileImage,
              double goalWeight, UserStatus userStatus) {
            this.username = username;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.goalWeight = goalWeight;
            this.userStatus = userStatus;
        }
    }

    @Data
    public static class UserFastingResponseDto {

        private LocalTime startFasting;
        private LocalTime endFasting;

        public UserFastingResponseDto(User user) {
            this.startFasting = user.getStartFasting();
            this.endFasting = user.getEndFasting();
        }

        public UserFastingResponseDto(LocalTime startFasting, LocalTime endFasting) {
            this.startFasting = startFasting;
            this.endFasting = endFasting;
        }
    }

}
