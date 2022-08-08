package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int likeIt;

    private Long postId;

    private Long userId;

    // Self Join
    private Long reCommentId;
}
