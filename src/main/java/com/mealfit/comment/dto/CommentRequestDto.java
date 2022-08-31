package com.mealfit.comment.dto;


import com.mealfit.comment.domain.Comment;
import com.mealfit.post.domain.Post;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CommentRequestDto {

    private Long commentId;
    private String comment;
    private Long postId;

    public Comment toEntity(Post post) {
        Comment comment1 = new Comment(comment,postId);
        comment1.addCommentToPost(post);
        return comment1;
    }


}
