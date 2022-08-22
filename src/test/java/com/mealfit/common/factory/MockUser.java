package com.mealfit.common.factory;

import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserNutritionGoal;
import com.mealfit.user.domain.UserStatus;
import java.time.LocalTime;

public class MockUser {

    private MockUser() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String username;
        private String password;
        private String nickname;
        private String email;
        private String profileImage = "https://github.com/testImage.jpg";
        private double goalWeight = 90.0;
        private LocalTime startFasting = LocalTime.now();
        private LocalTime endFasting = LocalTime.now();
        private UserStatus userStatus;
        private ProviderType providerType;
        private UserNutritionGoal userNutritionGoal;
        private double kcal;
        private double carbs;
        private double protein;
        private double fat;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Builder goalWeight(double goalWeight) {
            this.goalWeight = goalWeight;
            return this;
        }

        public Builder userStatus(UserStatus userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public Builder providerType(ProviderType providerType) {
            this.providerType = providerType;
            return this;
        }

        public Builder kcal(double kcal) {
            this.kcal = kcal;
            return this;
        }

        public Builder profileImage(double carbs) {
            this.carbs = carbs;
            return this;
        }

        public Builder protein(double protein) {
            this.protein = protein;
            return this;
        }

        public Builder fat(double fat) {
            this.fat = fat;
            return this;
        }

        public User build() {
            return new User(
                  id,
                  username, password, nickname, email, profileImage,
                  goalWeight,
                  startFasting, endFasting,
                  userStatus,
                  providerType,
                  new UserNutritionGoal(kcal, carbs, protein, fat)
            );
        }
    }
}
