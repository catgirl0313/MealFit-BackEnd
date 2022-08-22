package com.mealfit.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "ec2-13-125-227-9.ap-northeast-2.compute.amazonaws.com")
@ExtendWith(RestDocumentationExtension.class)
class CommonDocControllerTest{

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    public void errorSample() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        SampleRequest sampleRequest = new SampleRequest(
//              "name", "hhh.naver");
//        mockMvc.perform(post("/test/error")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(sampleRequest))
//              )
//              .andExpect(status().isBadRequest())
//              .andDo(document("common-error",
//                    preprocessResponse(prettyPrint()),
//                    responseFields(
//                          fieldWithPath("status").description("HTTP Status"),
//                          fieldWithPath("code").description("서비스에서 제공하는 에러 코드"),
//                          fieldWithPath("message").description("에러 메시지"),
//                          fieldWithPath("detail").description("에러 메시지 상세 설명")
//                    )));
//    }
}