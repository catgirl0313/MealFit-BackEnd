package com.mealfit.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.config.security.jwt.JwtUtils;
import com.mealfit.user.dto.request.SignUpRequestDto;
import com.mealfit.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "ec2-13-125-227-9.ap-northeast-2.compute.amazonaws.com")
@ExtendWith(RestDocumentationExtension.class)
class UserInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        saveUser();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    private void saveUser() {
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
              .username("test")
              .email("test@gmail.com")
              .password("qwe123123")
              .passwordCheck("qwe123123")
              .nickname("test1")
              .profileImage(null)
              .currentWeight(80.0)
              .goalWeight(75.1)
              .startFasting(LocalTime.now())
              .endFasting(LocalTime.now())
              .build();

        userRepository.save(signUpRequestDto.toEntity());

    }

    private String createJwtToken(String username) {
        return jwtUtils.issueAccessToken(username);
    }

    @DisplayName(value = "[GET] /info 요청 시 회원 정보를 반환한다.")
    @Test
    void 회원_정보_반환() throws Exception {
        String jwtToken = createJwtToken("test");

        mockMvc.perform(get("/user/info")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andDo(print())
              .andDo(document("user-getInfo",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                          fieldWithPath("userProfile.username").type(String.class)
                                .description("회원아이디"),
                          fieldWithPath("userProfile.nickname").type(String.class)
                                .description("닉네임"),
                          fieldWithPath("userProfile.profileImage").type(String.class)
                                .description("프로필 사진"),
                          fieldWithPath("userProfile.goalWeight").type(double.class)
                                .description("목표 무게"),
                          fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                .description("단식 시작 시간"),
                          fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                .description("단식 종료 시간")
                    )));
    }

    @DisplayName(value = "[POST] /info 요청 시 회원 정보를 수정한다.")
    @Test
    void 회원_정보_수정() throws Exception {
        String jwtToken = createJwtToken("test");

        MockMultipartFile image = new MockMultipartFile("profileImage", "profileTest.jpeg",
              "image/jpeg", "<<image-data>>".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/user/info")
                    .file(image)
                    .param("nickname", "new_Nickname")
                    .param("currentWeight", "80")
                    .param("goalWeight", "70")
                    .param("startFasting", "15:00")
                    .param("endFasting", "17:00")
                    .param("kcal", "2000")
                    .param("carbs", "250")
                    .param("protein", "150")
                    .param("fat", "50")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
              )
              .andExpect(status().isOk())
              .andDo(print())
              .andDo(document("user-changeInfo",
                    preprocessRequest(prettyPrint()),
                    requestParameters(
                          parameterWithName("nickname").optional().description("닉네임"),
                          parameterWithName("currentWeight").optional().description("현재 무게")
                                .attributes(key("constraints").value("0보다 커야 함.")),
                          parameterWithName("goalWeight").optional().description("목표 무게")
                                .attributes(key("constraints").value("0보다 커야 함")),
                          parameterWithName("startFasting").optional().description("단식 시작 시간")
                                .attributes(key("constraints").value("HH:mm 형태여야 함.")),
                          parameterWithName("endFasting").optional().description("단식 종료 시간")
                                .attributes(key("constraints").value("HH:mm 형태여야 함.")),
                          parameterWithName("kcal").optional().description("칼로리")
                                .attributes(key("constraints").value("0 이상이어야 함.")),
                          parameterWithName("carbs").optional().description("탄수화물")
                                .attributes(key("constraints").value("0 이상이어야 함.")),
                          parameterWithName("protein").optional().description("단백질")
                                .attributes(key("constraints").value("0 이상이어야 함.")),
                          parameterWithName("fat").optional().description("지방").attributes()
                                .attributes(key("constraints").value("0 이상이어야 함."))
                    ), requestParts(
                          partWithName("profileImage").optional().description("프로필 사진")
                    )));
    }
}