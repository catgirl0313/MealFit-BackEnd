package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CommentRequestDto {

    private Long commentId;
    private String comment;
    private Long postId;

    public Comment toEntity() {
        return new Comment(comment,postId);
    }


}
