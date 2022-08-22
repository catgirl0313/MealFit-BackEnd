package com.mealfit.post.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String nickname;
    private String profile;

    public UserDto(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }
}