package com.mealfit.user.controller;

import com.mealfit.BodyInfo.dto.request.BodyInfoSaveRequestDto;
import com.mealfit.BodyInfo.service.BodyInfoService;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.controller.dto.UserControllerDtoFactory;
import com.mealfit.user.controller.dto.request.ChangeUserInfoRequest;
import com.mealfit.user.controller.dto.request.ChangeUserPasswordRequest;
import com.mealfit.user.controller.dto.response.UserInfoResponse;
import com.mealfit.user.service.UserInfoService;
import com.mealfit.user.service.dto.UserServiceDtoFactory;
import com.mealfit.user.service.dto.request.ChangeUserInfoRequestDto;
import com.mealfit.user.service.dto.request.ChangeUserPasswordRequestDto;
import com.mealfit.user.service.dto.response.UserInfoResponseDto;
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
    public ResponseEntity<UserInfoResponse> userInfo(
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        UserInfoResponseDto responseDto = userInfoService.showUserInfoByUsername(userDetailsImpl.getUsername());
        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }

    // PUT -> multipart-form 의 경우 POST가 일반적.
    @PostMapping("/info")
    public ResponseEntity<String> changeUserInfo(@Valid ChangeUserInfoRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

        ChangeUserInfoRequestDto dto = UserServiceDtoFactory.ChangeUserInfoRequestDto(
              userDetailsImpl.getUsername(), request);

        userInfoService.changeUserInfo(dto);

        //TODO: 무게가 info에 있어야 하나? -> 분리하자
        bodyInfoService.saveBodyInfo(userDetailsImpl.getUser(),
              new BodyInfoSaveRequestDto(dto.getCurrentWeight(), 0, LocalDate.now()));

        return ResponseEntity.status(HttpStatus.OK)
              .body("수정 완료");
    }

    @PostMapping("/password")
    public ResponseEntity<UserInfoResponse> changePassword(@Valid ChangeUserPasswordRequest request,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChangeUserPasswordRequestDto dto = UserServiceDtoFactory.changeUserPasswordRequestDto(
              userDetails.getUsername(), request);

        UserInfoResponseDto responseDto = userInfoService.changePassword(dto);

        UserInfoResponse response = UserControllerDtoFactory.userInfoResponse(responseDto);

        return ResponseEntity.status(HttpStatus.OK)
              .body(response);
    }
}
