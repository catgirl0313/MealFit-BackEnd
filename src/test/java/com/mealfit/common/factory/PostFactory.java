package com.mealfit.common.factory;

import com.mealfit.post.domain.Post;
import com.mealfit.post.presentation.dto.request.PostRequest;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostFactory {

    private PostFactory() {
    }

    public static class Builder {

    }

    public static Post simplePost(long postId, long userId, String content) {
        return Post.builder()
              .id(postId)
              .userId(userId)
              .content(content)
              .build();
    }

    public static Post createPost(User user) {
        return Post.builder()
              .id(1L)
              .content("mocking content")
              .userId(user.getId())
              .nickname(user.getUserProfile().getNickname())
              .profileImage(user.getUserProfile().getProfileImage())
              .likeIt(0)
              .view(0)
              .images(new ArrayList<>())
              .build();
    }

    public static Post createPostByNoUser() {
        return Post.builder()
              .id(1L)
              .content("mocking content")
              .likeIt(0)
              .view(0)
              .images(new ArrayList<>())
              .build();
    }

    public static PostRequest createMockPostRequestDto() {
        PostRequest postRequest = new PostRequest();
        postRequest.setContent("mocking content");
        postRequest.setPostImageList(new ArrayList<>());
        return postRequest;
    }

    public static PostResponse createMockPostResponseDto() {
        return PostResponse.builder()
              .postId(1L)
              .nickname("testUser")
              .profileImage("http://testImage.t/profile")
              .content("mocking content")
              .like(0)
              .view(0)
              .images(new ArrayList<>())
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .liked(null)
              .build();
    }

    public static List<PostResponse> createMockPostResponseDtoList() {
        PostResponse dto = PostResponse.builder()
              .postId(1L)
              .nickname("testUser")
              .profileImage("http://testImage.t/profile")
              .content("mocking content")
              .like(0)
              .view(0)
              .images(new ArrayList<>())
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .liked(null)
              .build();
        return List.of(dto);
    }

}
