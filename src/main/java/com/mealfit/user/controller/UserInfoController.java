package com.mealfit.user.controller;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.User;
import com.mealfit.user.dto.UserInfoChangeRequestDto;
import com.mealfit.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserInfoController {

    private final UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<User> userInfo(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        // TODO : Entity -> DTO 로 변경 예정
        return ResponseEntity.status(HttpStatus.OK)
              .body(userDetailsImpl.getUser());
    }

    @PutMapping("/info")
    public ResponseEntity<User> changeUserInfo(UserInfoChangeRequestDto dto,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        userService.changeUserInfo(userDetailsImpl.getUsername(), dto);
        return ResponseEntity.status(HttpStatus.OK)
              .body(userDetailsImpl.getUser());
    }
}
