package com.mealfit.comment.presentation.dto.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UpdateCommentRequest {

    @NotBlank(message = "수정할 내용을 입력해주세요")
    private String content;
}
