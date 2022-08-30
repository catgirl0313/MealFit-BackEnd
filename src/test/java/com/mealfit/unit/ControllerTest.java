package com.mealfit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.bodyInfo.service.BodyInfoService;
import com.mealfit.comment.controller.CommentController;
import com.mealfit.comment.service.CommentService;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.post.controller.PostController;
import com.mealfit.post.service.PostService;
import com.mealfit.user.application.EmailService;
import com.mealfit.user.application.UserService;
import com.mealfit.user.presentation.UserAdminController;
import com.mealfit.user.presentation.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = {
      UserAdminController.class,
      UserController.class,
      PostController.class,
      CommentController.class,
}, excludeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@ActiveProfiles("test")
public abstract class ControllerTest {

    @MockBean
    protected JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    protected BodyInfoService bodyInfoService;

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected StorageService storageService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected UserService userService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}

