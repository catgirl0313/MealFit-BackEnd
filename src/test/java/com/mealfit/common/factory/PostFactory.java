package com.mealfit.common.factory;

import com.mealfit.post.domain.Post;
import com.mealfit.post.dto.PostRequestDto;
import com.mealfit.post.dto.PostResponseDto;
import com.mealfit.post.dto.UserDto;
import com.mealfit.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostFactory {

    private PostFactory() {
    }

    public static class Builder {

    }

    public static Post createPost(User user) {
        return Post.builder()
              .id(1L)
              .content("mocking content")
              .userId(user.getId())
              .nickName(user.getNickname())
              .likeIt(0)
              .view(0)
              .profileImage(user.getProfileImage())
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

    public static PostRequestDto createMockPostRequestDto() {
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContent("mocking content");
        postRequestDto.setPostImageList(new ArrayList<>());
        return postRequestDto;
    }

    public static PostResponseDto createMockPostResponseDto() {
        return PostResponseDto.builder()
              .postId(1L)
              .userDto(new UserDto("testUser", "profileImgUrl"))
              .content("mocking content")
              .like(0)
              .view(0)
              .image(new ArrayList<>())
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .liked(null)
              .build();
    }

    public static List<PostResponseDto> createMockPostResponseDtoList() {
        PostResponseDto dto = PostResponseDto.builder()
              .postId(1L)
              .userDto(new UserDto("testUser", "http://testImage.t/profile"))
              .content("mocking content")
              .like(0)
              .view(0)
              .image(new ArrayList<>())
              .createdAt(LocalDateTime.now())
              .updatedAt(LocalDateTime.now())
              .liked(null)
              .build();
        return List.of(dto);
    }

}
