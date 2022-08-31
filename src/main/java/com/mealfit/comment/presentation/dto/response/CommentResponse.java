package com.mealfit.comment.presentation.dto.response;


import com.mealfit.comment.domain.Comment;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter

public class CommentResponse {
    private Long commentId;
    private Long postId;
    private String comment;
    private UserInfoDto userDto;
    private int like;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.comment = comment.getContent();
        this.postId = comment.getPostId();
        this.userDto = new UserInfoDto(userDto.getProfileImage(), userDto.getNickname());
        this.like = comment.getLikeIt();
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserInfoDto {
        private String profileImage;
        private String nickname;
    }
}
