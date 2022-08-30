package com.mealfit.user.application;

import com.mealfit.bodyInfo.domain.BodyInfo;
import com.mealfit.bodyInfo.repository.BodyInfoRepository;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.DuplicatedSignUpException;
import com.mealfit.exception.user.NoUserException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.user.application.dto.UserServiceDtoFactory;
import com.mealfit.user.application.dto.request.ChangeNutritionRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.application.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindPasswordRequestDto;
import com.mealfit.user.application.dto.request.FindUsernameRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto;
import com.mealfit.user.application.dto.request.SendEmailRequestDto.EmailType;
import com.mealfit.user.application.dto.request.UserSignUpRequestDto;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.FastingTime;
import com.mealfit.user.domain.Nutrition;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.domain.email.EmailCertificationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailCertificationRepository emailCertificationRepository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;
    private final BodyInfoRepository bodyInfoRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,}$");

    @Transactional
    public UserInfoResponseDto signup(UserSignUpRequestDto dto) {
        validateSignUpDto(dto);

        User user = dto.toEntity();

        user.changePassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getProfileImage() != null) {
            List<String> profileUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "/profile");
            user.changeProfileImage(profileUrl.get(0));
        }

        // 분리하고 싶었으나 트랜잭션 전파때문에 어쩔 수 없이 한곳에 묶음.
        SendEmailRequestDto sendEmailDto = UserServiceDtoFactory.sendEmailRequestDto(
              dto.getUsername(), dto.getRedirectURL(), dto.getEmail(), EmailType.VALID_NEW_ACCOUNT);

        emailService.sendEmail(sendEmailDto);

        User saveEntity = userRepository.save(user);

        bodyInfoRepository.save(BodyInfo.createBodyInfo(saveEntity.getId(), dto.getCurrentWeight(),
              0, LocalDate.now()));

        return UserServiceDtoFactory.userInfoResponseDto(saveEntity);
    }

    private void validateSignUpDto(UserSignUpRequestDto dto) {
        validateUsername(dto.getUsername());
        validateNickname(dto.getNickname());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword(), dto.getPasswordCheck());
    }

    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicatedSignUpException("이미 존재하는 아이디입니다.");
        }
    }

    public void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedSignUpException("이미 존재하는 닉네임입니다.");
        }
    }

    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedSignUpException("이미 존재하는 이메일입니다.");
        }
    }

    public void validatePassword(String password, String passwordCheck) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new PasswordCheckException(
                  "비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다.");
        }
        if (!password.equals(passwordCheck)) {
            throw new PasswordCheckException("비밀번호와 비밀번호 재확인이 일치하지 않습니다.");
        }
    }

    @Transactional
    public UserInfoResponseDto changeUserInfo(ChangeUserInfoRequestDto dto) {
        User changeUser = findByUsername(dto.getUsername());

        if (dto.getProfileImage() != null) {
            String imageUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "profile/").get(0);
            changeUser.changeProfileImage(imageUrl);
        }

        changeUser.changeNickname(dto.getNickname());

        if (changeUser.checkFirstLogin()) {
            changeUser.changeUserStatus(UserStatus.NORMAL);
        }

        changeUser.changeGoalWeight(dto.getGoalWeight());

        changeUser.changeFastingTime(new FastingTime(
              dto.getStartFasting(), dto.getEndFasting()));

        changeUser.changeNutrition(
              new Nutrition(dto.getKcal(),
                    dto.getCarbs(),
                    dto.getProtein(),
                    dto.getFat()));

        return UserServiceDtoFactory.userInfoResponseDto(changeUser);
    }

    public UserInfoResponseDto showUserInfo(String username) {
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
        validatePassword(dto.getChangePassword(), dto.getCheckPassword());

        User user = findByUsername(dto.getUsername());

        if (!user.getLoginInfo().getPassword().equals(dto.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.changePassword(encodedPassword);

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    public void findUsername(FindUsernameRequestDto dto) {
        User user = userRepository.findByEmail(dto.getSendingEmail())
              .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));

        String maskedUsername = maskingUsername(user.getLoginInfo().getUsername());

        SendEmailRequestDto sendEmailRequestDto = UserServiceDtoFactory.sendEmailRequestDto(
              maskedUsername, dto.getRedirectUrl(), dto.getSendingEmail(),
              EmailType.FIND_USERNAME);

        emailService.sendEmail(sendEmailRequestDto);
    }

    private String maskingUsername(String username) {
        String middleMask = username.substring(3, username.length() - 1);
        return username.replace(middleMask, "*");
    }

    public void findPassword(FindPasswordRequestDto dto) {
        User user = findByUsername(dto.getUsername());

        if (!user.getUserProfile().getEmail().equals(dto.getSendingEmail())) {
            throw new IllegalArgumentException("잘못된 이메일입니다.");
        }

        SendEmailRequestDto sendEmailRequestDto = UserServiceDtoFactory.sendEmailRequestDto(
              dto.getUsername(), dto.getRedirectUrl(), dto.getSendingEmail(),
              EmailType.FIND_PASSWORD);


        emailService.sendEmail(sendEmailRequestDto);
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
              .orElseThrow(() -> new NoUserException("찾으려는 회원이 없습니다."));
    }

    @Transactional
    public UserInfoResponseDto changeNutrition(ChangeNutritionRequestDto dto) {
        User user = findByUsername(dto.getUsername());

        user.changeNutrition(
              new Nutrition(dto.getKcal(), dto.getCarbs(), dto.getProtein(), dto.getFat()));

        return UserServiceDtoFactory.userInfoResponseDto(user);
    }
}
