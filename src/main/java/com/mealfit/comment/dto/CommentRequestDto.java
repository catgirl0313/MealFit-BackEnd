package com.mealfit.comment.dto;

import com.mealfit.comment.domain.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
   private String content;

    public Comment toEntity() {
        return new Comment(content);

    }
}
