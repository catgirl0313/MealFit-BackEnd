package com.mealfit.diet.repository;

import com.mealfit.diet.domain.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DietRepository extends JpaRepository<Diet, Long> {
    Diet findByDate(LocalDate date);
}
