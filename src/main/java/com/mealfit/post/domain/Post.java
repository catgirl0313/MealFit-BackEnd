package com.mealfit.post.domain;


import com.mealfit.common.baseEntity.BaseEntity;

import java.util.*;
import javax.persistence.*;


import com.mealfit.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@ToString(exclude = "postImages")
@Getter
@NoArgsConstructor
@DynamicUpdate
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String nickName;

    @Column
    private String content;

    //조회수
    @Column
    private int view;

    @Column
    private int likeIt;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PostImage> images = new HashSet<>();


    public Post(String content) {
        this.content = content;
        this.likeIt = 0;
        this.view = 0;
    }

    public void settingUserInfo(Long userId,String profileImage,String nickName) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickName=nickName;
    }

    public void addPostImages(List<PostImage> images) {
        for (PostImage postImage : images) {
            addPostImage(postImage);
        }
    }
    private void addPostImage(PostImage postImage) {
        postImage.setPost(this);
        this.images.add(postImage);
    }

    public void update(PostRequestDto postRequestDto) {

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
