package com.mealfit.post.dto;

import lombok.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostsResponseDto {
    private Long postId;
    private String image;
    private String content;
    private int like;
    private int view;
}