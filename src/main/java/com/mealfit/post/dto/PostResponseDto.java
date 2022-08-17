package com.mealfit.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostResponseDto {
    private Long postId;
    MemberDto member;
    private String content;
    private int like;
    private int view;
    private List<String> image;

}
