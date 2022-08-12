package com.mealfit.diet.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.diet.dto.PostRequestDto;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;

@Getter
@Entity
public class Diet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DietStatus status;

    @Column(nullable = false)
    private LocalDate dietDate;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long foodId;

    private Long foodWeight;

    public Diet(Long foodId, DietStatus status, Long foodWeight, LocalDate dietDate) {
        this.foodId = foodId;
        this.status = status;
        this.foodWeight = foodWeight;
        this.dietDate = dietDate;
    }

    protected Diet() {

    }

    public void settingUserInfo(Long userId) {
        this.userId = userId;
    }
    public void update(PostRequestDto postRequestDto) {

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diet diet = (Diet) o;
        return Objects.equals(id, diet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
