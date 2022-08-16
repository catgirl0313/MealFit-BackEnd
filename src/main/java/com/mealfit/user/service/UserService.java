package com.mealfit.user.service;

import com.mealfit.common.email.EmailUtil;
import com.mealfit.common.email.FindPasswordEmail;
import com.mealfit.common.email.SignUpEmail;
import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.loginJwtSocial.dto.LoginIdCheckDto;
import com.mealfit.user.domain.EmailCertification;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.repository.EmailCertificationRepository;
import com.mealfit.user.repository.UserRepository;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,}$");
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final EmailCertificationRepository emailCertificationRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
          EmailUtil emailUtil, EmailCertificationRepository emailCertificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        validateUsername(dto.getUsername());
        validateNickname(dto.getNickname());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword(), dto.getPasswordCheck());
    }

    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
    }

    public void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    private void sendValidLink(String email, String username, String url) {
        //TODO javax.email 전송 -> AWS SNS
        String certificationCode = UUID.randomUUID().toString();
        emailUtil.sendEmail(email, new SignUpEmail(url, username, certificationCode));
        emailCertificationRepository.save(new EmailCertification(username, certificationCode));
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

    private void validatePassword(String password, String passwordCheck) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                  ("비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다."));
        }
        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
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

    //로그인 유저 정보 반환
    public LoginIdCheckDto userInfo(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        String usernickname = userDetails.getMember().getNickname();
        LoginIdCheckDto userinfo = new LoginIdCheckDto(username, usernickname);
        return userinfo;
    }
}
