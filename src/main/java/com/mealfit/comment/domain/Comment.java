package com.mealfit.comment.domain;

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

    // Self Join
    private Long reCommentId;


    }

