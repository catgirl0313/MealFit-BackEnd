package com.mealfit.loginJwtSocial.social;

import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.loginJwtSocial.dto.LoginIdCheckDto;
import com.mealfit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final KakaoService kakaoService;
    private final UserService userService;


    //카카오 소셜 로그인
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<String> kakaoCalback(String code, HttpServletResponse response) {      //ResponseBody -> Data를 리턴해주는 컨트롤러 함수
        kakaoService.requestKakao(code, response);
        return ResponseEntity.status(HttpStatus.OK)
                .body("소셜 로그인 완료!");
    }

    //로그인 유저 정보
    @GetMapping("user/login/auth")
    public LoginIdCheckDto userDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.userInfo(userDetails);
    }


}

//    // 회원 가입 요청 처리
//    @PostMapping("/user/signup")
//    public String registerUser(@Valid @RequestBody SignupRequestDto requestDto) {
//        String res = userService.registerUser(requestDto);
//        if (res.equals("")) {
//            return "회원가입 성공";
//        } else {
//            return res;
//        }
//    }
//


