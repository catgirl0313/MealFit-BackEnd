package com.mealfit.unit.user.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "ec2-13-125-227-9.ap-northeast-2.compute.amazonaws.com")
@ExtendWith(RestDocumentationExtension.class)
class UserValidationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    public static void setUp(@Autowired UserRepository userRepository) {
//        userRepository.deleteAll();
//
//        // 회원 저장
//        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
//              .username("test")
//              .email("test@gmail.com")
//              .password("qwe123123")
//              .passwordCheck("qwe123123")
//              .nickname("test1")
//              .profileImage(null)
//              .currentWeight(80.0)
//              .goalWeight(75.1)
//              .startFasting(LocalTime.now())
//              .endFasting(LocalTime.now())
//              .build();
//
//        userRepository.save(signUpRequestDto.toEntity());
//    }
//
//    @AfterAll
//    public static void tearDown(@Autowired UserRepository userRepository) {
//        userRepository.deleteAll();
//    }
//
//    @DisplayName(value = "[GET] /user/username")
//    @Test
//    void 회원_아이디_중복_확인() throws Exception {
//        mockMvc.perform(get("/user/username")
//                    .queryParam("username", "test123"))
//              .andExpect(status().isOk())
//              .andDo(print())
//              .andDo(document("user-usernameValidation",
//                    preprocessRequest(prettyPrint()),
//                    requestParameters(
//                          parameterWithName("username").description("중복이 없을 아이디")
//                    )));
//    }
//
//    @DisplayName(value = "[GET] /user/email")
//    @Test
//    void 회원_이메일_중복_확인() throws Exception {
//        mockMvc.perform(get("/user/email")
//                    .queryParam("email", "test123@gmail.com"))
//              .andExpect(status().isOk())
//              .andDo(print())
//              .andDo(document("user-emailValidation",
//                    preprocessRequest(prettyPrint()),
//                    requestParameters(
//                          parameterWithName("email").description("중복이 없을 이메일")
//                    )));
//    }
//
//    @DisplayName(value = "[GET] /user/nickname")
//    @Test
//    void 회원_닉네임_중복_확인() throws Exception {
//        mockMvc.perform(get("/user/nickname")
//                    .queryParam("nickname", "test2"))
//              .andExpect(status().isOk())
//              .andDo(print())
//              .andDo(document("user-nicknameValidation",
//                    preprocessRequest(prettyPrint()),
//                    requestParameters(
//                          parameterWithName("nickname").description("중복이 없을 닉네임")
//                    )));
//    }
}