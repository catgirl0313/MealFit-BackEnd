package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String comment;
    
    private int likeIt;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String profileImage;


    public Comment(String comment,Long postId) {
        this.comment = comment;
        this.postId = postId;
        this.likeIt = 0;
    }

    public void settingUserInfo(Long userId,String profileImage) {
        this.userId = userId;
        this.profileImage = profileImage;
    }

    public void update(String comment){
        this.comment = comment;
    }
}

