package com.mealfit.user.service;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.s3.S3Service;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.request.UserInfoChangeRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, EmailUtil emailUtil,
          EmailCertificationRepository emailCertificationRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
        this.emailCertificationRepository = emailCertificationRepository;
        this.s3Service = s3Service;
    }

    public void findUsername(String url, String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("잘못된 이메일입니다.");
        }

        String authKey = UUID.randomUUID().toString();

        emailUtil.sendEmail(email, new FindPasswordEmail(url, email, authKey));
    }

    public void findPassword(String url, String email, String username) {
        User user = userRepository.findByEmail(email)
              .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));

        if (!user.getUsername().equals(username)) {
            throw new IllegalArgumentException("요청정보가 일치하지 않습니다.");
        }

        String authKey = UUID.randomUUID().toString();

        emailUtil.sendEmail(email, new FindPasswordEmail(url, email, authKey));
    }

    public void checkValidLink(String username, String authKey) {
        User user = userRepository.findByEmail(username)
              .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));

        // 이후 인증을 받으면 User 상태를 FIRST_LOGIN으로 변환.
        if (user.getUserStatus() != UserStatus.NOT_VALID) {
            throw new IllegalStateException("이미 인증을 완료했습니다.");
        }

        if (!emailCertificationRepository.existsByUsernameAndAuthKey(username, authKey)) {
            throw new IllegalStateException("잘못된 요청입니다.");
        }

        user.setUserStatus(UserStatus.FIRST_LOGIN);
    }

    @Transactional
    public void changeUserInfo(String username, UserInfoChangeRequestDto dto) {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new IllegalArgumentException("다시 로그인해주세요"));

        String imageUrl = null;

        // TODO: S3 연결 이후 변경
//        if (dto.getProfileImage() != null) {
//            imageUrl = s3Service.uploadOne(dto.getProfileImage(), "PROFILE");
//        }

        user.updateInfo(dto.getNickname(), imageUrl);
        user.setUserStatus(UserStatus.NORMAL);
        user.changeFastingTime(dto.getStartFasting(), dto.getEndFasting());
        user.updateUserNutrition(dto.getKcal(),
              dto.getCarbs(),
              dto.getProtein(),
              dto.getFat());
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto findUserInfo(String username) {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new IllegalArgumentException("다시 로그인해주세요"));

        return new UserInfoResponseDto(user);
    }
}
