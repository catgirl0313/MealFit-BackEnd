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
    private Member member;
    private String content;
    private int likeCnt;
    private List<String> imageUrls;

    public PostCUDResponseDto(Post post, User user, List<String> images) {
        this.postId = post.getId();
        this.member = new Member(
                user.getUsername(),
                user.getNickname(),
                user.getProfileImage());
        this.content = post.getContent();
        this.likeCnt = post.getLikeIt();
        this.imageUrls = images;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Member {
        private String username;
        private String nickname;
        private String profileImage;
    }
}

