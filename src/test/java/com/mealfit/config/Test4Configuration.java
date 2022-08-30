package com.mealfit.config;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.MockEmailUtil;
import com.mealfit.common.storageService.MockStorageService;
import com.mealfit.common.storageService.StorageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class Test4Configuration {

    @Bean
    public StorageService storageService() {
        return new MockStorageService();
    }

    @Bean
    public EmailUtil emailUtil() {
        return new MockEmailUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance() ;
    }
}
