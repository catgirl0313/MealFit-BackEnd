package com.mealfit.comment.domain;

import com.mealfit.common.baseEntity.BaseEntity;


import com.mealfit.post.domain.Post;
import com.mealfit.user.domain.User;
import lombok.*;


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

    @Column(nullable = false)
    private String nickName;

    @Setter
    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;

    public Comment(String comment,Long postId) {
        this.comment = comment;
        this.postId = postId;
        this.likeIt = 0;
    }

    public void settingUserInfo(Long userId,String profileImage,String nickName) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }

    public void update(String comment){
        this.comment = comment;
    }

    public void addCommentToPost(Post post) {
        post.addComment(this);
    }

}
