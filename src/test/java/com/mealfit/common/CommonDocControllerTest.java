package com.mealfit.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.common.CommonDocController.SampleRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

    @Test
    public void errorSample() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SampleRequest sampleRequest = new SampleRequest(
              "name", "hhh.naver");
        mockMvc.perform(post("/test/error")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sampleRequest))
              )
              .andExpect(status().isBadRequest())
              .andDo(document("common-error",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                          fieldWithPath("status").description("HTTP Status"),
                          fieldWithPath("code").description("서비스에서 제공하는 에러 코드"),
                          fieldWithPath("message").description("에러 메시지"),
                          fieldWithPath("detail").description("에러 메시지 상세 설명")
                    )));
    }
}