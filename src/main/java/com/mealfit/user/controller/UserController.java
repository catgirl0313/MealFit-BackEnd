package com.mealfit.user.controller;

import com.mealfit.user.dto.SignUpRequestDto;
import com.mealfit.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> signup(HttpServletRequest request, @Valid SignUpRequestDto dto) {
        userService.signup(extractDomainRoot(request), dto);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("가입 완료!");
    }

    /**
     * 검증 부분
     */
    @PostMapping("/username")
    public ResponseEntity<String> validateUsername(@RequestBody String username) {
        userService.validateUsername(username);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @PostMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestBody String email) {
        userService.validateEmail(email);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    @PostMapping("/nickname")
    public ResponseEntity<String> validateNickname(@RequestBody String nickname) {
        userService.validateNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK)
              .body("검증완료!");
    }

    /**
     * 이메일 인증
     */
    @GetMapping("/validate")
    public ResponseEntity<String> checkEmail(String username, String authKey) {
        userService.checkValidLink(username, authKey);

        return ResponseEntity.status(HttpStatus.CREATED)
              .body("이메일이 성공적으로 인증되었습니다.");
    }

    private String extractDomainRoot(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    }

    @GetMapping("/find/username")
    public ResponseEntity<String> findUsername(HttpServletRequest request, @RequestBody String email) {
        userService.findUsername(extractDomainRoot(request), email);
        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }

    @GetMapping("/find/password")
    public ResponseEntity<String> findPassword(HttpServletRequest request, @RequestBody String email, String username) {
        userService.findPassword(extractDomainRoot(request), email, username);
        return ResponseEntity.status(HttpStatus.OK)
              .body("전송완료");
    }
}
