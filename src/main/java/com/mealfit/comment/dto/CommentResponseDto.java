package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter

public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private String comment;
    private UserInfoDto userDto;
    private int like;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.postId = comment.getPostId();
        this.userDto = new UserInfoDto(userDto.getProfileImage(), userDto.getNickname());
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserInfoDto {
        private String profileImage;
        private String nickname;
    }
}
