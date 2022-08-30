package com.mealfit.post.dto;

import com.mealfit.post.domain.Post;
import com.mealfit.user.domain.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCUDResponseDto {

    private Long postId;
    private UserInfoDto userInfoDto;
    private String content;
    private int likeCnt;
    private List<String> imageUrls;

    public PostCUDResponseDto(Post post, User user) {
        this.postId = post.getId();
        this.userInfoDto = new UserInfoDto(
              user.getUserProfile().getNickname(),
              user.getUserProfile().getProfileImage());
        this.content = post.getContent();
        this.likeCnt = post.getLikeIt();
    }

    public PostCUDResponseDto(Post post, User user, List<String> imageUrls) {
        this.postId = post.getId();
        this.userInfoDto = new UserInfoDto(
              user.getUserProfile().getNickname(),
              user.getUserProfile().getProfileImage());
        this.content = post.getContent();
        this.likeCnt = post.getLikeIt();
        this.imageUrls = imageUrls;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserInfoDto {
        private String nickname;
        private String profileImage;
    }
}

