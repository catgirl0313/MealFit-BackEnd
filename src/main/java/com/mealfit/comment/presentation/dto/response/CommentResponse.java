package com.mealfit.comment.presentation.dto.response;


import com.mealfit.comment.domain.Comment;

import lombok.*;


@ToString
@NoArgsConstructor
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
        this.userDto = new UserInfoDto(comment.getNickname(), comment.getProfileImage());
        this.like = comment.getLikeIt();
    }

    @Data
    @AllArgsConstructor
    static class UserInfoDto {

        private String nickname;
        private String profileImage;
    }
}
