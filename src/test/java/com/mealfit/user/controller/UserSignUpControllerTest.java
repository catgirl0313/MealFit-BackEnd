package com.mealfit.user.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
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
class UserSignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @DisplayName(value = "[POST] /user/signup 요청 시 회원가입이 ")
    @Test
    void 회원_가입() throws Exception {

        MockMultipartFile image = new MockMultipartFile("profileImage",
              "profileTest.jpeg", "image/jpeg",
              "<<image data>>".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/user/signup")
                    .file(image)
                    .param("username", "test123")
                    .param("email", "test@gmail.com")
                    .param("password", "qwe123123")
                    .param("passwordCheck", "qwe123123")
                    .param("nickname", "nickname")
                    .param("currentWeight", "80")
                    .param("goalWeight", "70")
                    .param("startFasting", "15:00")
                    .param("endFasting", "17:00")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
              )
              .andExpect(status().isCreated())
              .andDo(print())
              .andDo(document("user-signup",
                    preprocessRequest(prettyPrint()),
                    requestParameters(
                          parameterWithName("username").description("아이디"),
                          parameterWithName("email").description("이메일")
                                .attributes(key("constraints").value("이메일 형식")),
                          parameterWithName("password").description("비밀번호")
                                .attributes(key("constraints").value("숫자 영문 포함 8자리 이상")),
                          parameterWithName("passwordCheck").description("비밀번호 확인")
                                .attributes(key("constraints").value("비밀번호와 동일해야 함.")),
                          parameterWithName("nickname").description("닉네임"),
                          parameterWithName("currentWeight").description("현재 몸무게")
                                .attributes(key("constraints").value("0보다 커야 함.")),
                          parameterWithName("goalWeight").description("목표 몸무게")
                                .attributes(key("constraints").value("0보다 커야 함.")),
                          parameterWithName("startFasting").description("단식 시작 시간")
                                .attributes(key("constraints").value("HH:mm 형태여야 함.")),
                          parameterWithName("endFasting").description("단식 종료 시간")
                                .attributes(key("constraints").value("HH:mm 형태여야 함."))
                    ), requestParts(
                          partWithName("profileImage").optional().description("프로필 사진")
                    )));

        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(userRepository.findAll().get(0).getUsername()).isEqualTo("test123");
    }
}