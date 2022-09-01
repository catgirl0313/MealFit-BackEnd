package com.mealfit.post.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriterDto {

    private String nickname;
    private String profileImage;

    public WriterDto(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}