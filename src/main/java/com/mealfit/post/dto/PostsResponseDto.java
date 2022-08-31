package com.mealfit.post.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostsResponseDto {
    private Long postId;
    private String image;
    UserDto userDto;
    private String content;
    private int like;
    private int view;


}