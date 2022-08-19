package com.mealfit.user.controller;

import com.mealfit.user.service.UserValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserValidationController {

    private final UserValidationService userValidationService;

    public UserValidationController(UserValidationService userValidationService) {
        this.userValidationService = userValidationService;
    }

    @GetMapping("/username")
    public ResponseEntity<String> validateUsername(@RequestParam String username) {
        userValidationService.validateUsername(username);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @GetMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) {
        userValidationService.validateEmail(email);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> validateNickname(@RequestParam String nickname) {
        userValidationService.validateNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

}
