package com.mealfit.comment.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder

public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private String comment;
    UserDto userDto;
    private int like;

}
