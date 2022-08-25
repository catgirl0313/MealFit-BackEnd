package com.mealfit.post.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String nickname;
    private String profile;

    public MemberDto(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;


    }
}