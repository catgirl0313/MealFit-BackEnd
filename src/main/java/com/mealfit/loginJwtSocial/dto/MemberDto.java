package com.mealfit.loginJwtSocial.dto;

import com.mealfit.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String profileImage;
    private double currentWeight;
    private double goalWeight;
    private LocalTime startFasting;
    private LocalTime endFasting;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}