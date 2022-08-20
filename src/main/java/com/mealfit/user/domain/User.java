package com.mealfit.user.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.common.crypt.CryptoConverter;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    private String nickname;

    // 개인을 특정할 수 있는 정보들은 모두 암호화를 해야 합니다.
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Setter
    private String profileImage;

    @Column
    private double goalWeight;

    @Column
    private LocalTime startFasting;

    @Column
    private LocalTime endFasting;

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Setter
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

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

    private User(String username, String password, String nickname, String email,
          double goalWeight
          , LocalTime startFasting, LocalTime endFasting, UserNutritionGoal userNutritionGoal) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
        this.userStatus = UserStatus.NOT_VALID;
        this.providerType = ProviderType.LOCAL;
        this.userNutritionGoal = userNutritionGoal;
    }

    private User(String username, String password,
          String nickname, String email,
          ProviderType providerType,
          UserNutritionGoal userNutritionGoal) {

        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.goalWeight = 0;
        this.startFasting = null;
        this.endFasting = null;
        this.userStatus = UserStatus.FIRST_SOCIAL_LOGIN;
        this.providerType = providerType;
        this.userNutritionGoal = userNutritionGoal;
    }

    public static User createLocalUser(String username, String password, String nickname,
          String email,
          double currentWeight, double goalWeight
          , LocalTime startFasting, LocalTime endFasting) {
        return new User(username, password, nickname, email,
              goalWeight, startFasting, endFasting,
              new UserNutritionGoal(0, 0, 0, 0));
    }

    public static User createSocialUser(String username, String password, String nickname,
          String email,
          ProviderType providerType) {
        return new User(username, password, nickname, email, providerType,
              new UserNutritionGoal(0, 0, 0, 0));
    }

    public User updateProfile(String nickname, String picture) {
        this.nickname = nickname;
        this.profileImage = picture;
        return this;
    }

    public void updateInfo(String nickname, String picture) {
        this.nickname = nickname;
        this.profileImage = picture;
    }

    public void updateUserNutrition(double kcal, double carbs, double protein, double fat) {
        userNutritionGoal.updateUserNutritionGoal(kcal, carbs, protein, fat);
    }

    public void changeFastingTime(LocalTime startFasting, LocalTime endFasting) {
        this.startFasting = startFasting;
        this.endFasting = endFasting;
    }
}
