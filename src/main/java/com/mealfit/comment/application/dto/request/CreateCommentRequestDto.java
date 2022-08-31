package com.mealfit.comment.application.dto.request;

import com.mealfit.comment.domain.Comment;
import lombok.Getter;

@Getter
public class CreateCommentRequestDto {

    private String content;
    private Long postId;
    private Long userId;
    private String nickname;
    private String profileImage;

    public CreateCommentRequestDto(String content, Long postId, Long userId, String nickname,
          String profileImage) {
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public Comment toEntity() {
        Comment comment = new Comment(content, postId);
        comment.settingUserInfo(userId, nickname, profileImage);
        return comment;
    }
}
