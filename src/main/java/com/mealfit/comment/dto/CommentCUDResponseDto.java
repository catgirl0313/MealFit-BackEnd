package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;
import com.mealfit.post.domain.Post;
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
    CommentUserDto userDto;
    private int like;

    public CommentCUDResponseDto(Comment comment, User user){
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.postId = comment.getPostId();
        this.userDto = new CommentUserDto(
                user.getUserProfile().getNickname(),
                user.getUserProfile().getProfileImage());
        this.like = comment.getLikeIt();

    }


}
