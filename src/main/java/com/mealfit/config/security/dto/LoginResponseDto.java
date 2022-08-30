package com.mealfit.config.security.dto;

import com.mealfit.config.security.details.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private TokenBox tokenBox;
    private String userStatus;
    private String providerType;

    public LoginResponseDto(String accessToken, String refreshToken, UserDetailsImpl userDetails) {
        this.tokenBox = new TokenBox(accessToken, refreshToken);
        this.userStatus = userDetails.getUser().getUserStatusInfo().getUserStatus().name();
        this.providerType = userDetails.getUser().getUserStatusInfo().getProviderType().name();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class TokenBox {
        private String accessToken;
        private String refreshToken;
    }
}
