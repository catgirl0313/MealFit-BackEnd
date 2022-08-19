package com.mealfit.user.dto;


import com.mealfit.user.domain.User;
import java.io.Serializable;
import java.time.LocalTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoChangeRequestDto implements Serializable {

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

    private UserNutritionGoalDto userNutritionGoalDto;

    public UserInfoChangeRequestDto(String nickname, MultipartFile profileImage,
          double currentWeight,
          double goalWeight, LocalTime startFasting, LocalTime endFasting,
          double kcal, double carbs, double protein, double fat) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
        this.userNutritionGoalDto = new UserNutritionGoalDto(kcal, carbs, protein, fat);
    }

    @Data
    public static class UserNutritionGoalDto {

        @Min(value = 0, message = "0보다 크게 입력해주세요")
        private double kcal;

        @Min(value = 0, message = "0보다 크게 입력해주세요")
        private double carbs;

        @Min(value = 0, message = "0보다 크게 입력해주세요")
        private double protein;

        @Min(value = 0, message = "0보다 크게 입력해주세요")
        private double fat;

        public UserNutritionGoalDto(double kcal, double carbs, double protein, double fat) {
            this.kcal = kcal;
            this.carbs = carbs;
            this.protein = protein;
            this.fat = fat;
        }

        public UserNutritionGoalDto(User user) {
            this.kcal = user.getUserNutritionGoal().getKcal();
            this.carbs = user.getUserNutritionGoal().getCarbs();
            this.protein = user.getUserNutritionGoal().getProtein();
            this.fat = user.getUserNutritionGoal().getFat();
        }
    }

}
