package com.mealfit.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUserDto {

    private String nickname;
    private String profileImage;

    public CommentUserDto(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
