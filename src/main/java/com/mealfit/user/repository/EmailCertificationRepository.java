package com.mealfit.user.repository;

import com.mealfit.user.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {

    boolean existsByUsernameAndAuthKey(String username, String authKey);
}
