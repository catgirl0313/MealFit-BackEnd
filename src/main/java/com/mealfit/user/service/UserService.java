package com.mealfit.user.service;

import com.mealfit.user.domain.User;
import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.repository.UserRepository;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,}$");
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(SignUpRequestDto dto) {
        validateSignUpDto(dto);

        User user = dto.toEntity();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (dto.getProfileImage() != null) {
            // TODO 사진 저장 로직
        }

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

    private void validatePassword(String password, String passwordCheck) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(
                  ("비밀번호는 최소 8자리에 숫자, 영어 문자를 각각 1개 이상 포함해야 합니다."));
        }
        if (!password.equals(passwordCheck)) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
    }
}
