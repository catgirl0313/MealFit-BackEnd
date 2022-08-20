package com.mealfit.user.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mealfit.user.domain.User;
import com.mealfit.user.dto.request.SignUpRequestDto;
import com.mealfit.user.repository.UserRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserSignUpServiceTest {

    @Autowired
    private UserSignUpService userSignUpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @DisplayName(value = "1. 회원가입 테스트")
    @Test
    void 회원가입_테스트(){
        LocalTime testingTime = LocalTime.now();
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
              .username("test")
              .email("test@gmail.com")
              .password("qwe123123")
              .passwordCheck("qwe123123")
              .nickname("test1")
              .profileImage(null)
              .currentWeight(80.0)
              .goalWeight(75.1)
              .startFasting(testingTime)
              .endFasting(testingTime)
              .build();

        userSignUpService.signup("TEST_DOMAIN_URL", signUpRequestDto);

        assertEquals(1L, userRepository.count());
        assertDoesNotThrow(() -> userRepository.findByUsername("test"));

        User savedUser = userRepository.findByUsername("test").get();

        assertEquals("test", savedUser.getUsername());
        assertEquals("test@gmail.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("qwe123123", savedUser.getPassword()));
        assertEquals("test1", savedUser.getNickname());
        assertNull(savedUser.getProfileImage());
        assertEquals(75.1, savedUser.getGoalWeight());
        assertEquals(testingTime, savedUser.getStartFasting());
        assertEquals(testingTime, savedUser.getEndFasting());
    }
}