package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private String content;
    private String profileImage;
    private int like;

    public CommentResponseDto(Comment comment) {
    }
}
