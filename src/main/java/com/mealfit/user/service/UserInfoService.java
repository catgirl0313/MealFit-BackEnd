package com.mealfit.user.service;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.NoUserException;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)

@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;
    private final StorageService storageService;

    public UserInfoService(UserRepository userRepository, EmailUtil emailUtil,
          EmailCertificationRepository emailCertificationRepository,
          StorageService storageService) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
        this.emailCertificationRepository = emailCertificationRepository;
        this.storageService = storageService;
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

        user.changeUserStatus(UserStatus.FIRST_LOGIN);
    }

    @Transactional
    public UserInfoResponseDto changeUserInfo(String username, ChangeUserInfoRequestDto dto) {
        User changeUser = userRepository.findByUsername(username)
              .orElseThrow(() -> new NoUserException("찾으려는 회원이 없습니다."));

        String imageUrl = null;

        if (dto.getProfileImage() != null) {
            imageUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "PROFILE").get(0);
        }

        changeUser.changeProfile(dto.getNickname(), imageUrl);
        changeUser.changeGoalWeight(dto.getGoalWeight());

        if (changeUser.getUserStatus() == UserStatus.FIRST_LOGIN) {
            changeUser.changeUserStatus(UserStatus.NORMAL);
        }

        changeUser.changeFastingTime(dto.getStartFasting(), dto.getEndFasting());
        changeUser.changeUserNutrition(dto.getKcal(),
              dto.getCarbs(),
              dto.getProtein(),
              dto.getFat());

        return new UserInfoResponseDto(changeUser);
    }

    public UserInfoResponseDto findUserInfo(String username) {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new NoUserException("찾으려는 회원이 없습니다."));

        return new UserInfoResponseDto(user);
    }

    public List<UserInfoResponseDto> findUserInfoList() {

        return userRepository.findAll()
              .stream()
              .map(UserInfoResponseDto::new)
              .collect(Collectors.toList());
    }
}
