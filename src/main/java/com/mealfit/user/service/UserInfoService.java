package com.mealfit.user.service;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.NoUserException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import com.mealfit.user.service.dto.UserServiceDtoFactory;
import com.mealfit.user.service.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.service.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.service.dto.response.UserInfoResponseDto;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;


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
    public UserInfoResponseDto changeUserInfo(ChangeUserInfoRequestDto dto) {
        User changeUser = findByUsername(dto.getUsername());

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

        return UserServiceDtoFactory.userInfoResponseDto(changeUser);
    }

    public UserInfoResponseDto showUserInfoByUsername(String username) {
        User user = findByUsername(username);

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    public List<UserInfoResponseDto> showUserInfoList() {

        return userRepository.findAll()
              .stream()
              .map(UserServiceDtoFactory::userInfoResponseDto)
              .collect(Collectors.toList());
    }

    @Transactional
    public UserInfoResponseDto changePassword(ChangeUserPasswordRequestDto dto) {
        User user = findByUsername(dto.getUsername());

        if (!dto.getPassword().equals(dto.getPasswordCheck())) {
            throw new PasswordCheckException("비밀번호와 비밀번호 재확인이 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.changePassword(encodedPassword);

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
              .orElseThrow(() -> new NoUserException("찾으려는 회원이 없습니다."));
    }

}
