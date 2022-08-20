package com.mealfit.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealfit.user.domain.User;
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

    @JsonProperty("userProfile")
    private UserProfileResponseDto userProfile;

    @JsonProperty("fastingInfo")
    private UserFastingResponseDto fastingInfo;

    public UserInfoResponseDto(User user) {
        this.userProfile = new UserProfileResponseDto(user);
        this.fastingInfo = new UserFastingResponseDto(user);
    }

    @Data
    static class UserProfileResponseDto {

        private String username;
        private String nickname;
        private String profileImage;
        private double goalWeight;


        public UserProfileResponseDto(User user) {
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.profileImage = user.getProfileImage();
            this.goalWeight = user.getGoalWeight();
        }
    }

    @Data
    static class UserFastingResponseDto {
        private LocalTime startFasting;
        private LocalTime endFasting;

        public UserFastingResponseDto(User user) {
            this.startFasting = user.getStartFasting();
            this.endFasting = user.getEndFasting();
        }
    }

}
