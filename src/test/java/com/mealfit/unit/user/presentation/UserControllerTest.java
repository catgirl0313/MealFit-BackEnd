package com.mealfit.unit.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mealfit.common.factory.UserFactory;
import com.mealfit.config.security.WithMockCustomUser;
import com.mealfit.unit.ControllerTest;
import com.mealfit.user.application.dto.response.UserInfoResponseDto;
import com.mealfit.user.domain.ProviderType;
import com.mealfit.user.domain.UserStatus;
import com.mealfit.user.presentation.dto.response.UserInfoResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

class UserControllerTest extends ControllerTest {

    UserInfoResponseDto userInfoResponseDto = UserFactory.mockUserInfoResponseDto(
          1L, "username",
          "nickname", "https://github.com/profileImg", "test@gmail.com",
          80.0, LocalTime.of(11, 0), LocalTime.of(12, 0),
          2000, 200, 150, 50,
          UserStatus.NORMAL, ProviderType.LOCAL);

    UserInfoResponse userInfoResponse = UserFactory.mockUserInfoResponse(
          1L,
          "nickname", "https://github.com/profileImg", "test@gmail.com",
          80.0, LocalTime.of(11, 0), LocalTime.of(12, 0),
          2000, 200, 150, 50,
          UserStatus.NORMAL, ProviderType.LOCAL);

    @DisplayName("로그인 되어 있을 때 ")
    @Nested
    class Context_when_Login {

        @DisplayName(value = "[POST] /user/info 요청 시 회원 정보를 수정할 수 있다.")
        @WithMockCustomUser
        @Test
        void changeUserInfo_success() throws Exception {

            // given
            MockMultipartFile image = new MockMultipartFile("profileImage", "profileTest.jpeg",
                  "image/jpeg", "<<image-data>>".getBytes(StandardCharsets.UTF_8));

            given(userService.changeUserInfo(any())).willReturn(userInfoResponseDto);

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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changeInfo",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레싴 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ),
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

            verify(userService, times(1))
                  .changeUserInfo(any());
        }

        @DisplayName(value = "[POST] /user/social/signup 요청 시 소셜 회원의 최초 정보 기입을 할 수 있다.")
        @WithMockCustomUser
        @Test
        void socialSignup_success() throws Exception {

            // given
            MockMultipartFile image = new MockMultipartFile("profileImage", "profileTest.jpeg",
                  "image/jpeg", "<<image-data>>".getBytes(StandardCharsets.UTF_8));

            given(userService.changeUserInfo(any())).willReturn(userInfoResponseDto);

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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf().asHeader())
                  )
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-changeInfo",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                              headerWithName(HttpHeaders.AUTHORIZATION).description("엑세스 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰.")),
                              headerWithName("refresh_token").description("리프레싴 토큰")
                                    .attributes(key("constraints").value("Bearer 토큰."))
                        ),
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

            verify(userService, times(1))
                  .changeUserInfo(any());
        }

        @DisplayName(value = "[GET] /user/info 요청 시 회원 정보를 반환한다.")
        @WithMockCustomUser
        @Test
        void userInfo_success() throws Exception {

            //given
            given(userService.showUserInfo(anyString())).willReturn(userInfoResponseDto);

            mockMvc.perform(get("/user/info")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer access_token")
                        .header("refresh_token", "Bearer refresh_token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf().asHeader()))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-getInfo",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                              fieldWithPath("userId").type(String.class)
                                    .description("회원 아이디"),
                              fieldWithPath("userProfile.nickname").type(String.class)
                                    .description("닉네임"),
                              fieldWithPath("userProfile.profileImage").type(String.class)
                                    .description("프로필 사진"),
                              fieldWithPath("userProfile.goalWeight").type(double.class)
                                    .description("목표 무게"),
                              fieldWithPath("userProfile.userStatus").type(String.class)
                                    .description("현재 유저 상태 (미인증, 최초 로그인, 일반"),
                              fieldWithPath("userProfile.providerType").type(String.class)
                                    .description("로그인 정보 (로컬, KAKAO, NAVER, GOOGLE)"),
                              fieldWithPath("fastingInfo.startFasting").type(LocalTime.class)
                                    .description("단식 시작 시간"),
                              fieldWithPath("fastingInfo.endFasting").type(LocalTime.class)
                                    .description("단식 종료 시간"),
                              fieldWithPath("nutritionGoal.kcal").type(double.class)
                                    .description("목표 섭취 칼로리"),
                              fieldWithPath("nutritionGoal.carbs").type(double.class)
                                    .description("목표 섭취 탄수화물"),
                              fieldWithPath("nutritionGoal.protein").type(double.class)
                                    .description("목표 섭취 단백질"),
                              fieldWithPath("nutritionGoal.fat").type(double.class)
                                    .description("목표 섭취 지방")
                        )));

            verify(userService, times(1))
                  .showUserInfo(anyString());
        }
    }

    @DisplayName("로그인 되어 있지 않을 때 ")
    @Nested
    class Context_when_not_Login {

        @DisplayName(value = "[POST] /user/signup 요청 시 회원가입에 성공한다.")
        @WithMockCustomUser
        @Test
        void signup_success() throws Exception {

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
                        .with(csrf().asHeader()))
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
        }
        @DisplayName(value = "[GET] /user/{username}/{아이디} 요청 시 아이디 중복 확인을 시도한다.")
        @WithMockCustomUser
        @Test
        void validateUsername_success() throws Exception {
            mockMvc.perform(get("/user/{key}/{value}", "username", "test123@gmail.com"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-usernameValidation",
                        preprocessRequest(prettyPrint())));
        }

        @DisplayName(value = "[GET] /user/{email}/{이메일} 요청 시 이메일 중복 확인을 시도한다.")
        @WithMockCustomUser
        @Test
        void validateEmail_success() throws Exception {
            mockMvc.perform(get("/user/{key}/{value}", "nickname", "test123@gmail.com"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-emailValidation",
                        preprocessRequest(prettyPrint())));
        }

        @DisplayName(value = "[GET] /user/{nickname}/{닉네임} 요청 시 닉네임 중복 확인을 시도한다.")
        @WithMockCustomUser
        @Test
        void validateNickname_success() throws Exception {
            mockMvc.perform(get("/user/{key}/{value}", "nickname", "test2"))
                  .andExpect(status().isOk())
                  .andDo(print())
                  .andDo(document("user-nicknameValidation",
                        preprocessRequest(prettyPrint())));
        }
    }
}