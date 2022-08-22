package com.mealfit.user.controller;

import com.mealfit.user.service.UserInfoService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserFindController {

    private final UserInfoService userInfoService;

    public UserFindController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/find/username")
    public ResponseEntity<String> findUsername(HttpServletRequest request, @RequestBody String email) {
        userInfoService.findUsername(extractDomainRoot(request), email);
        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }

    @GetMapping("/find/password")
    public ResponseEntity<String> findPassword(HttpServletRequest request, @RequestBody String email, String username) {
        userInfoService.findPassword(extractDomainRoot(request), email, username);
        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }

    private String extractDomainRoot(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    }
}
