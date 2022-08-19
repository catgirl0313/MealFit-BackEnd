package com.mealfit.user.service;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.SignUpEmail;
import com.mealfit.user.domain.EmailCertification;
import com.mealfit.user.domain.User;
import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;

    public UserSignUpService(UserRepository userRepository,
          PasswordEncoder passwordEncoder, UserValidationService userValidationService,
          EmailUtil emailUtil, EmailCertificationRepository emailCertificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidationService = userValidationService;
        this.emailUtil = emailUtil;
        this.emailCertificationRepository = emailCertificationRepository;
    }

    @Transactional
    public void signup(String domainURL, SignUpRequestDto dto) {
        validateSignUpDto(dto);

        User user = dto.toEntity();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (dto.getProfileImage() != null) {
            // TODO 사진 저장 로직
        }

        sendValidLink(dto.getEmail(), dto.getUsername(), domainURL);
        userRepository.save(user);
    }

    private void validateSignUpDto(SignUpRequestDto dto) {
        userValidationService.validateUsername(dto.getUsername());
        userValidationService.validateNickname(dto.getNickname());
        userValidationService.validateEmail(dto.getEmail());
        userValidationService.validatePassword(dto.getPassword(), dto.getPasswordCheck());
    }

    private void sendValidLink(String email, String username, String url) {
        //TODO javax.email 전송 -> AWS SNS
        String certificationCode = UUID.randomUUID().toString();
        emailUtil.sendEmail(email, new SignUpEmail(url, username, certificationCode));
        emailCertificationRepository.save(new EmailCertification(username, certificationCode));
    }
}
