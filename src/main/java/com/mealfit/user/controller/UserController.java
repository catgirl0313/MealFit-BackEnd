package com.mealfit.user.controller;

import com.mealfit.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserInfoService userInfoService;

    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 이메일 인증
     */
    @GetMapping("/validate")
    public ResponseEntity<String> checkEmail(String username, String authKey) {
        userInfoService.checkValidLink(username, authKey);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("이메일이 성공적으로 인증되었습니다.");
    }
}
