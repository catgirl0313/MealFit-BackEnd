package com.mealfit.user.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, Long> {

    boolean existsByUsernameAndAuthKey(String username, String authKey);
}