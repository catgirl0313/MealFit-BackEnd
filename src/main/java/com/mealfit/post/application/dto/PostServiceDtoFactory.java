package com.mealfit.post.application.dto;

import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.presentation.dto.request.PostRequest;
import com.mealfit.user.domain.User;

public class PostServiceDtoFactory {

    public static PostCreateRequestDto postCreateRequestDto(PostRequest request, User user) {
        return PostCreateRequestDto.builder()
              .content(request.getContent())
              .postImageList(request.getPostImageList())
              .userId(user.getId())
              .nickname(user.getUserProfile().getNickname())
              .profileImage(user.getUserProfile().getProfileImage())
              .build();
    }

    public static PostUpdateRequestDto postUpdateRequestDto(Long postId, PostRequest request, User user) {
        return PostUpdateRequestDto.builder()
              .postId(postId)
              .content(request.getContent())
              .postImageList(request.getPostImageList())
              .userId(user.getId())
              .build();
    }
}
