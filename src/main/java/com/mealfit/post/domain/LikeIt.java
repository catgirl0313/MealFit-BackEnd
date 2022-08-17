package com.mealfit.post.domain;


import com.mealfit.common.baseEntity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "like_it",
        indexes = {
                @Index(columnList = "username"),
                @Index(columnList = "postId")
        })
@Entity
public class LikeIt extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String username;

    private Long postId;

}
