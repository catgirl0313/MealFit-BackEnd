package com.mealfit.user.controller;

import com.mealfit.BodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.BodyInfo.service.BodyInfoService;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.dto.response.UserInfoResponseDto;
import com.mealfit.user.service.UserInfoService;
import java.time.LocalDate;
import javax.validation.Valid;
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

    private final UserInfoService userInfoService;
    private final BodyInfoService bodyInfoService;
    public UserInfoController(UserInfoService userInfoService, BodyInfoService bodyInfoService) {
        this.userInfoService = userInfoService;
        this.bodyInfoService = bodyInfoService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> userInfo(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseEntity.status(HttpStatus.OK)
              .body(new UserInfoResponseDto(userDetailsImpl.getUser()));
    }

    // PUT -> multipart-form 의 경우 POST가 일반적.
    @PostMapping("/info")
    public ResponseEntity<String> changeUserInfo(@Valid ChangeUserInfoRequestDto dto,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        userInfoService.changeUserInfo(userDetailsImpl.getUsername(), dto);
        bodyInfoService.saveBodyInfo(userDetailsImpl.getUser(),
              new BodyInfoSaveRequestDto(dto.getCurrentWeight(), 0, LocalDate.now()));

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료");
    }
}
