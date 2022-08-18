package com.mealfit.comment.domain;

import com.mealfit.comment.dto.CommentRequestDto;
import com.mealfit.common.baseEntity.BaseEntity;

import lombok.Getter;


import javax.persistence.*;

@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    private int likeIt;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String profileImage;
    // Self Join???
    private Long reCommentId;


    public Comment(String content) {
        this.content = content;
        this.likeIt = 0;
    }

    public Comment() {
    }
    public void settingUserInfo(Long userId,String profileImage) {
        this.userId = userId;
        this.profileImage = profileImage;
    }

    public void update(CommentRequestDto commentDto) {
    }
}

