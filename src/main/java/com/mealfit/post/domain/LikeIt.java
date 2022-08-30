package com.mealfit.post.domain;


import com.mealfit.common.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long postId;



}
