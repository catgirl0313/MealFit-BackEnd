package com.mealfit.user.domain;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Embeddable
public class UserBasicProfile {

    @Column(nullable = false, unique = true,  updatable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false, unique = true)
    private String nickname;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    private String profileImage = "BASIC";

    @Setter
    private double goalWeight;

    @Setter
    private LocalTime startFasting;

    @Setter
    private LocalTime endFasting;

    protected UserBasicProfile() {}

    @Builder
    public UserBasicProfile(String username, String password, String nickname, String email,
          String profileImage, double goalWeight, LocalTime startFasting, LocalTime endFasting) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
