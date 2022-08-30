package com.mealfit.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserStatusInfo {

    @Setter
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    public UserStatusInfo(UserStatus userStatus, ProviderType providerType) {
        this.userStatus = userStatus;
        this.providerType = providerType;
    }
}
