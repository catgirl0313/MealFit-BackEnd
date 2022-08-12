package com.mealfit.user.dto;

import com.mealfit.user.domain.User;
import java.io.Serializable;
import java.time.LocalTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto implements Serializable {

    @NotBlank private String username;
    @NotBlank @Email private String email;
    @NotBlank private String password;
    @NotBlank private String passwordCheck;
    @NotBlank private String nickname;
    private MultipartFile profileImage;
    @NotNull private double currentWeight;
    @NotNull private double goalWeight;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startFasting;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endFasting;

    public User toEntity() {
        return new User(username, password, nickname, email, currentWeight, goalWeight,
              startFasting, endFasting);
    }
}
