package com.mealfit.user.controller;

import com.mealfit.BodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.BodyInfo.service.BodyInfoService;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.dto.request.UserInfoChangeRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.service.UserService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserInfoController {

    private final UserService userService;
    private final BodyInfoService bodyInfoService;
    public UserInfoController(UserService userService, BodyInfoService bodyInfoService) {
        this.userService = userService;
        this.bodyInfoService = bodyInfoService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> userInfo(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.status(HttpStatus.OK)
              .body(new UserInfoResponseDto(userDetailsImpl.getUser()));
    }

    @PostMapping("/info")
    public ResponseEntity<String> changeUserInfo(UserInfoChangeRequestDto dto,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        userService.changeUserInfo(userDetailsImpl.getUsername(), dto);
        bodyInfoService.saveBodyInfo(userDetailsImpl.getUser(),
              new BodyInfoSaveRequestDto(dto.getCurrentWeight(), LocalDate.now()));

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료");
    }
}
