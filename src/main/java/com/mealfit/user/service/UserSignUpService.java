package com.mealfit.user.service;

import com.mealfit.BodyInfo.domain.BodyInfo;
import com.mealfit.BodyInfo.repository.BodyInfoRepository;
import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.SignUpEmail;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.exception.user.DuplicatedSignUpException;
import com.mealfit.exception.user.PasswordCheckException;
import com.mealfit.user.domain.EmailCertification;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import com.mealfit.user.service.dto.UserServiceDtoFactory;
import com.mealfit.user.service.dto.request.UserSignUpRequestDto;
import com.mealfit.user.service.dto.response.UserInfoResponseDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserSignUpService {

    private final BodyInfoRepository bodyInfoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,}$");


    @Transactional
    public UserInfoResponseDto signup(UserSignUpRequestDto dto) {
        validateSignUpDto(dto);

        User user = dto.toEntity();

        user.changePassword(passwordEncoder.encode(user.getPassword()));

        if (dto.getProfileImage() != null) {
            List<String> profileUrl = storageService.uploadMultipartFile(
                  List.of(dto.getProfileImage()), "/profile");
            user.changeProfileImage(profileUrl.get(0));
        }


        // 분리하고 싶었으나 트랜잭션 전파때문에 어쩔 수 없이 한곳에 묶음.
        sendValidLink(dto.getEmail(), dto.getUsername(), dto.getRedirectURL());
        User saveEntity = userRepository.save(user);
        bodyInfoRepository.save(BodyInfo.createBodyInfo(saveEntity.getId(), dto.getCurrentWeight(),
              0, LocalDate.now()));
        return UserServiceDtoFactory.userInfoResponseDto(user);
    }

    private void validateSignUpDto(UserSignUpRequestDto dto) {
        validateUsername(dto.getUsername());
        validateNickname(dto.getNickname());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword(), dto.getPasswordCheck());
    }

    private void sendValidLink(String email, String username, String url) {
        //TODO javax.email 전송 -> AWS SNS
        String certificationCode = UUID.randomUUID().toString();
        emailUtil.sendEmail(email, new SignUpEmail(url, username, certificationCode));
        emailCertificationRepository.save(new EmailCertification(username, certificationCode));
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
}