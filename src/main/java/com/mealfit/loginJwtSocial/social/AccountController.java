package com.mealfit.loginJwtSocial.social;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.loginJwtSocial.auth.UserDetailsImpl;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AccountController{
    private final Constant.SocialLoginType socialLoginType;
//    private final OAuthService oAuthService;
    private final KakaoService kakaoService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.secret-key}")
    private String secretKey;

//        @ResponseBody String


    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<String> kakaoCallback(String code, HttpServletResponse response) { //@ResponseBody : Data를 리턴해주는 컨트롤러 함수, Query로 String code 받아

        //<토큰 발급 요청 주소>
        // POST 방식으로 key-value 데이터를 요청(카카오쪽으로)
        RestTemplate rt = new RestTemplate();
        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        //HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "e51136f184950f9ec0ccb7d4e1aad610");
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
        params.add("code", code);
        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = //body params와 headers값을 가진 entity가 된다.
                new HttpEntity<>(params, headers);
        //Http 요청하기 -POST 방식으로 - 그리고 response 변수의 응답 받음 실제 요청
        ResponseEntity<String> responseEntity = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );



        //json 데이터를 브젝트로 담는 라이브러리 -Gson, Json Simple, ObjectMapper-쉽고 기본 내장 되어있음.
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuthToken kakaoOAuthToken = null;
        try {
            kakaoOAuthToken = objectMapper.readValue(responseEntity.getBody(), KakaoOAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("카카오 엑세스 토큰 : " + kakaoOAuthToken.getAccess_token());//잘 찍히는 지 확인.
//        return response.getBody();



        // <토큰을 통한 사용자 정보 조회>
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+ kakaoOAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
                new HttpEntity<>(headers2);
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        System.out.println(response2.getBody());
        // return "카카오 토큰 요청 완료 :" + response.getBody();
        // return "엑세스 토큰으로 받은 사용자 정보 :" + response2.getBody();

        //json 데이터를 브젝트로 담는 라이브러리 -Gson, Json Simple, ObjectMapper-쉽고 기본 내장 되어있음.
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        //User 오브젝트 : username, password, email
        System.out.println("카카오 아이디(번호) :" + kakaoProfile.getId());
        System.out.println("카카오 이메일 :" + kakaoProfile.getKakao_account().getEmail());
        //카카오에서 받은 정보로 밀핏 회원 강제 로그인 시키기. 추가 정보가 필요하면 회원정보 구성창을 따로 만들어 추가 기입 받아줘야함.
        System.out.println("밀핏서버 유저네임 :" + kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("밀핏서버 이메일 :" + kakaoProfile.getKakao_account().getEmail());
        UUID tempPassword = UUID.randomUUID(); //임시 패스워드. garbage
        System.out.println("밀핏서버 패스워드 :" + tempPassword);

        //강제 회원가입 시키고싶은데 User domain으로 어떻게?
        User socialUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(tempPassword.toString())
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        //가입자 혹은 비가입자 체크해서 처리
        User originUser = kakaoService.findByUser(socialUser.getUsername());

        if(originUser.getUsername()==null){
            System.out.println("신규 회원입니다.");
            kakaoService.signupSocialUser(socialUser);
//            return false;
        }
        //로그인 처리
        // kakao 로그인 처리
        System.out.println("kakao 로그인 진행중");
        if (socialUser.getUsername() != null) {
            User userEntity = userRepository.findByUsername(socialUser.getUsername()).orElseThrow(
                    () -> new IllegalArgumentException("kakao username이 없습니다.")
            );
            UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

//            UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal(); //?

            //JWT 토큰 발급
            //RSA방식은 아니고 Hash암호 방식 //jwts : 무슨 문법?
            String jwtToken = JWT.create()
                    .withSubject("cos토큰")
                    .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
                    .withClaim("username", userDetails.getMember().getUsername())
                    .sign(Algorithm.HMAC512(secretKey));

            response.addHeader("Authorization", jwtToken); //헤더에 토큰을 넣어줘.

            System.out.println("JWT토큰 : " + jwtToken);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("로그인 완료!");
    }


//        return response2.getBody();
} // 인증코드발급 : 카카오의 개인정보에 접근하기 위한 access토큰 발급 준비 완료.


//
//    /**
//     * 유저 소셜 로그인으로 리다이렉트 해주는 url
//     * [GET] /accounts/auth
//     * @return void
//     */
////    @NoAuth
//    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
//    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
//        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
//        oAuthService.request(socialLoginType);
//    }
//
//    /**
//     * Social Login API Server 요청에 의한 callback 을 처리
//     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
//     * @param code API Server 로부터 넘어오는 code
//     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
//     */
//
////    @NoAuth
//    @ResponseBody
//    @GetMapping(value = "/auth/{socialLoginType}/callback")
//    public BaseResponse<getSocialOAuthRes> callback (
//            @PathVariable(name = "socialLoginType") String socialLoginPath,
//            @RequestParam(name = "code") String code)throws IOException { //,BaseException
//        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
//        Constant.SocialLoginType socialLoginType= Constant.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
//        GetSocialOAuthRes getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
//        System.out.println(getSocialOAuthRes);
//        return BaseResponse<>(getSocialOAuthRes);
//    }
//}
//    @PostMapping("/signup")
//    public ResponseEntity<String> signup(HttpServletRequest request, @Valid SignUpRequestDto dto) {
//        userService.signup(extractDomainRoot(request), dto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("가입 완료!");
//    }
//     return new BaseResponse<>(getSocialOAuthRes);