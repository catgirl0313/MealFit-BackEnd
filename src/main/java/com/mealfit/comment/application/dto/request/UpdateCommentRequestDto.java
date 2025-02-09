package com.mealfit.comment.application.dto.request;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {

    private Long commentId;
    private Long userId;
    private String content;

    public UpdateCommentRequestDto(String content, Long commentId, Long userId) {
        this.content = content;
        this.commentId = commentId;
        this.userId = userId;
    }
}
