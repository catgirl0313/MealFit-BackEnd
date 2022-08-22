package com.mealfit.user.dto.request;

import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserBasicProfile;
import java.io.Serializable;
import java.time.LocalTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto implements Serializable {

    @Size(max = 20)
    @NotBlank(message = "아이디는 필수로 입력해주세요")
    private String username;

    @NotBlank(message = "이메일은 필수로 입력해주세요")
    @Email(message = "이메일 형식을 확인해 주세요")
    private String email;

    @Size(max = 20)
    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String password;

    @Size(max = 20)
    @NotBlank(message = "비밀번호 재확인을 필수로 입력해주세요")
    private String passwordCheck;

    @Size(max = 20)
    @NotBlank(message = "닉네임을 필수로 입력해주세요")
    private String nickname;

    private MultipartFile profileImage;

    @NotNull(message = "현재 몸무게를 입력해주세요")
    private double currentWeight;

    @NotNull(message = "목표 몸무게를 입력해주세요")
    private double goalWeight;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startFasting;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endFasting;

    public User toEntity() {
        return User.createLocalUser(
              UserBasicProfile.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .email(email)
                    .goalWeight(goalWeight)
                    .startFasting(startFasting)
                    .endFasting(endFasting)
                    .build());
    }

    @Builder
    public SignUpRequestDto(String username, String email, String password, String passwordCheck,
          String nickname, MultipartFile profileImage, double currentWeight, double goalWeight,
          LocalTime startFasting, LocalTime endFasting) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
