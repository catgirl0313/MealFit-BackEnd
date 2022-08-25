package com.mealfit.user.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.UserStatus;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse implements Serializable {

    private Long userId;

    @JsonProperty("userProfile")
    private UserProfileResponse userProfile;

    @JsonProperty("fastingInfo")
    private UserFastingResponse fastingInfo;

    @Builder
    public UserInfoResponse(Long userId, String email, String nickname, String profileImage,
          double goalWeight, UserStatus userStatus, ProviderType providerType, LocalTime startFasting, LocalTime endFasting) {
        this.userId = userId;
        this.userProfile = new UserProfileResponse(email, nickname, profileImage,
              goalWeight, userStatus, providerType);
        this.fastingInfo = new UserFastingResponse(startFasting, endFasting);
    }

    @Data
    public static class UserProfileResponse {

        private String email;
        private String nickname;
        private String profileImage;
        private double goalWeight;
        private UserStatus userStatus;
        private String providerType;

        public UserProfileResponse(String email, String nickname, String profileImage,
              double goalWeight, UserStatus userStatus, ProviderType providerType) {

            this.email = email;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.goalWeight = goalWeight;
            this.userStatus = userStatus;
            this.providerType = providerType.name();
        }
    }

    @Data
    public static class UserFastingResponse {

        private LocalTime startFasting;
        private LocalTime endFasting;

        public UserFastingResponse(LocalTime startFasting, LocalTime endFasting) {
            this.startFasting = startFasting;
            this.endFasting = endFasting;
        }
    }

}
