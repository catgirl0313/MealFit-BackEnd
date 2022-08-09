package com.mealfit.user.controller;

import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(SignUpRequestDto dto) {
        userService.signup(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("생성완료!");
    }


    /**
     * 검증 부분
     */
    @PostMapping("/id")
    public ResponseEntity<String> validateUsername(@RequestBody String username) {
        userService.validateUsername(username);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @PostMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestBody String email) {
        userService.validateUsername(email);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @PostMapping("/nickname")
    public ResponseEntity<String> validateNickname(@RequestBody String nickname) {
        userService.validateNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }
}
