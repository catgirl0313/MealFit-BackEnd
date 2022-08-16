package com.mealfit.diet.repository;

import com.mealfit.diet.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRepository extends JpaRepository<Diet, Long> {
    List<Diet> findByDietDateAndUserId(LocalDate date, Long userId); // 해당 회원의 식단 조회
}
