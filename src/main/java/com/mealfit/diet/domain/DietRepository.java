package com.mealfit.diet.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByDietDateAndUserId(LocalDate date, Long userId); // 해당 회원의 식단 조회
}
