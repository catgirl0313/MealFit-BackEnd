package com.mealfit.user.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.exception.user.NoUserException;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicUpdate
@Table(name = "users", indexes = {
      @Index(columnList = "username"),
      @Index(columnList = "nickname"),
      @Index(columnList = "email")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LoginInfo loginInfo;

    @Embedded
    private UserProfile userProfile;

    private double goalWeight;

    @Embedded
    private FastingTime fastingTime;

    @Embedded
    private Nutrition nutrition;

    @Embedded
    private UserStatusInfo userStatusInfo;

    public User(LoginInfo loginInfo, UserProfile userProfile, double goalWeight,
          FastingTime fastingTime, Nutrition nutrition, UserStatusInfo userStatusInfo) {
        this.loginInfo = loginInfo;
        this.userProfile = userProfile;
        this.goalWeight = goalWeight;
        this.fastingTime = fastingTime;
        this.nutrition = nutrition;
        this.userStatusInfo = userStatusInfo;
    }

    public User(Long id, LoginInfo loginInfo, UserProfile userProfile, double goalWeight,
          FastingTime fastingTime, Nutrition nutrition, UserStatusInfo userStatusInfo) {
        this.id = id;
        this.loginInfo = loginInfo;
        this.userProfile = userProfile;
        this.goalWeight = goalWeight;
        this.fastingTime = fastingTime;
        this.nutrition = nutrition;
        this.userStatusInfo = userStatusInfo;
    }

    public static User createLocalUser(LoginInfo loginInfo, UserProfile userBasicProfile,
          double goalWeight,
          FastingTime fastingTime, Nutrition nutrition) {
        return new User(loginInfo,
              userBasicProfile,
              goalWeight,
              fastingTime,
              new Nutrition(0, 0, 0, 0),
              new UserStatusInfo(UserStatus.NOT_VALID, ProviderType.LOCAL));
    }

    public static User createSocialUser(LoginInfo loginInfo,
          UserProfile userBasicProfile, ProviderType providerType) {
        return new User(loginInfo,
              userBasicProfile,
              0,
              null,
              new Nutrition(0, 0, 0, 0),
              new UserStatusInfo(UserStatus.FIRST_LOGIN, providerType));
    }

    public void changePassword(String password) {
        this.loginInfo = new LoginInfo(this.loginInfo.getUsername(), password);
    }

    public void changeProfileImage(String profileImage) {
        checkUserDeleted();
        setUserProfile(new UserProfile(
              this.userProfile.getNickname(),
              this.userProfile.getEmail(),
              profileImage));
    }

    public void changeGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public void changeNickname(String nickname) {
        setUserProfile(new UserProfile(nickname,
              userProfile.getEmail(),
              userProfile.getProfileImage()));
    }

    @Generated
    private void setUserProfile(UserProfile userBasicProfile) {
        if (userBasicProfile == null) {
            throw new IllegalArgumentException("userBasicProfile is null");
        }
        this.userProfile = userBasicProfile;
    }

    @Generated
    private void setUserStatusInfo(UserStatusInfo userStatusInfo) {
        if (userStatusInfo == null) {
            throw new IllegalArgumentException("userBasicProfile is null");
        }

        this.userStatusInfo = userStatusInfo;
    }

    public void changeFastingTime(FastingTime fastingTime) {
        setFastingTime(fastingTime);
    }

    @Generated
    private void setFastingTime(FastingTime fastingTime) {
        if (fastingTime == null) {
            throw new IllegalArgumentException("fastingTime is null");
        }

        this.fastingTime = fastingTime;
    }

    public void changeNutrition(Nutrition nutrition) {
        setNutrition(nutrition);
    }

    @Generated
    private void setNutrition(Nutrition nutrition) {
        if (nutrition == null) {
            throw new IllegalArgumentException("nutrition is null");
        }

        this.nutrition = nutrition;
    }

    public void changeUserStatus(UserStatus userStatus) {
        this.userStatusInfo = new UserStatusInfo(userStatus, this.userStatusInfo.getProviderType());
    }

    public boolean checkNotValid() {
        return this.userStatusInfo.getUserStatus() == UserStatus.NOT_VALID;
    }

    public boolean checkFirstLogin() {
        return this.userStatusInfo.getUserStatus() == UserStatus.FIRST_LOGIN;
    }

    @Generated
    private void checkUserDeleted() {
        if (userStatusInfo.getUserStatus() == UserStatus.DELETE) {
            throw new NoUserException("이미 회원탈퇴한 회원입니다. userId = " + this.id);
        }
    }

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

}
