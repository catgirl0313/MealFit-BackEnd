package com.mealfit.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Embeddable
public class UserStatusInfo {

    @Setter
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    protected UserStatusInfo() {
    }

    public UserStatusInfo(UserStatus userStatus, ProviderType providerType) {
        this.userStatus = userStatus;
        this.providerType = providerType;
    }
}
