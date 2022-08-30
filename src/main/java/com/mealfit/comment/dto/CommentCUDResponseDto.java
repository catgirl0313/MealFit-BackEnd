package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;
import com.mealfit.user.domain.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCUDResponseDto {
    private Long commentId;
    private Long postId;
    private String comment;
    private UserInfoDto userInfoDto;
    private int like;

    public CommentCUDResponseDto(Comment comment, User user){
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.postId = comment.getPostId();
        this.userInfoDto = new UserInfoDto(
                user.getUserProfile().getNickname(),
                user.getUserProfile().getProfileImage());
        this.like = comment.getLikeIt();

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserInfoDto {
        private String nickname;
        private String profileImage;
    }

}
