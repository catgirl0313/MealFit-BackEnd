package com.mealfit.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes.Attribute;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public RestDocumentationResultHandler write(){
        return MockMvcRestDocumentation.document(
              "{class-name}/{method-name}",
              Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
              Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }

    // 제약조건용 Attribute
    public static final Attribute field(
          final String key,
          final String value){
        return new Attribute(key,value);
    }
}
