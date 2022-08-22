package com.mealfit.user.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Table(name = "users", indexes = {
      @Index(columnList = "username"),
      @Index(columnList = "nickname"),
      @Index(columnList = "email")
})
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserBasicProfile userBasicProfile;

    @Embedded
    private UserStatusInfo userStatusInfo;

    @Embedded
    private UserNutritionGoal userNutritionGoal;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected User() {
    }

    public User(Long id, UserBasicProfile userBasicProfile, UserStatusInfo userStatusInfo) {
        this.id = id;
        this.userBasicProfile = userBasicProfile;
        this.userStatusInfo = userStatusInfo;
        this.userNutritionGoal = new UserNutritionGoal(0, 0, 0, 0);
    }

    public User(Long id, UserBasicProfile userBasicProfile, UserStatusInfo userStatusInfo,
          UserNutritionGoal userNutritionGoal) {
        this.id = id;
        this.userBasicProfile = userBasicProfile;
        this.userStatusInfo = userStatusInfo;
        this.userNutritionGoal = userNutritionGoal;
    }

    public static User createLocalUser(UserBasicProfile userBasicProfile) {
        return new User(null, userBasicProfile,
              new UserStatusInfo(UserStatus.NOT_VALID, ProviderType.LOCAL),
              new UserNutritionGoal(0, 0, 0, 0));
    }

    public static User createSocialUser(UserBasicProfile userBasicProfile, ProviderType providerType) {
        return new User(null, userBasicProfile,
              new UserStatusInfo(UserStatus.NOT_VALID, providerType),
              new UserNutritionGoal(0, 0, 0, 0));
    }

    public void changeProfile(String nickname, String imageUrl) {
        changeNickname(nickname);
        changeProfileImage(imageUrl);
    }

    public void changeProfile(UserBasicProfile userBasicProfile) {
        this.userBasicProfile = userBasicProfile;
    }

    public void changeUserNutrition(double kcal, double carbs, double protein, double fat) {
        userNutritionGoal.updateUserNutritionGoal(kcal, carbs, protein, fat);
    }


    public void changeUserStatus(UserStatus userStatus) {
        this.userStatusInfo.setUserStatus(userStatus);
    }

    public void changePassword(String password) {
        this.userBasicProfile.setPassword(password);
    }

    public void changeNickname(String nickname) {
        this.userBasicProfile.setNickname(nickname);
    }

    public void changeEmail(String email) {
        this.userBasicProfile.setEmail(email);
    }

    public void changeProfileImage(String profileImage) {
        this.userBasicProfile.setProfileImage(profileImage);
    }

    public void changeFastingTime(LocalTime startFasting, LocalTime endFasting) {
        this.userBasicProfile.setStartFasting(startFasting);
        this.userBasicProfile.setEndFasting(endFasting);
    }

    public void changeGoalWeight(double goalWeight) {
        this.userBasicProfile.setGoalWeight(goalWeight);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return userBasicProfile.getUsername();
    }

    public String getPassword() {
        return userBasicProfile.getPassword();
    }

    public String getNickname() {
        return userBasicProfile.getNickname();
    }

    public String getEmail() {
        return userBasicProfile.getEmail();
    }

    public String getProfileImage() {
        return userBasicProfile.getProfileImage();
    }

    public double getGoalWeight() {
        return userBasicProfile.getGoalWeight();
    }

    public LocalTime getStartFasting() {
        return userBasicProfile.getStartFasting();
    }

    public LocalTime getEndFasting() {
        return userBasicProfile.getEndFasting();
    }

    public UserStatus getUserStatus() {
        return userStatusInfo.getUserStatus();
    }

    public ProviderType getProviderType() {
        return userStatusInfo.getProviderType();
    }

    public double getKcal() {
        return userNutritionGoal.getKcal();
    }

    public double getCarbs() {
        return userNutritionGoal.getCarbs();
    }

    public double getProtein() {
        return userNutritionGoal.getProtein();
    }

    public double getFat() {
        return userNutritionGoal.getFat();
    }
}
