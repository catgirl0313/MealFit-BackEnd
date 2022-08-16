package com.mealfit.user.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.common.crypt.CryptoConverter;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

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

    @Convert(converter = CryptoConverter.class)
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
    private double currentWeight;

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
    private OAuthType oAuthType;

    // 추후 변경 예정
    private double kcal;

    private double carbs;

    private double protein;

    private double fat;

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

    public User(String username, String password, String nickname, String email,
          double currentWeight, double goalWeight
          , LocalTime startFasting, LocalTime endFasting) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
        this.startFasting = startFasting;
        this.endFasting = endFasting;
        this.userStatus = UserStatus.NOT_VALID;
        this.oAuthType = OAuthType.NONE;
    }

    public User(String username, String password, String nickname, String email, String oauthType) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.currentWeight = 0;
        this.goalWeight = 0;
        this.startFasting = null;
        this.endFasting = null;
        this.userStatus = UserStatus.SIGNUP_SOCIAL;
        this.oAuthType = OAuthType.valueOf(oauthType);
    }

    public static User of(String username, String password, String nickname, String email,
          String oauthType) {
        return new User(username, password, nickname, email, oauthType);
    }

    public User update(String nickname, String picture) {
        this.nickname = nickname;
        this.profileImage = picture;
        return this;
    }
}
