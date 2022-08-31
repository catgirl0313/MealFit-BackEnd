package com.mealfit.post.domain;


import com.mealfit.comment.domain.Comment;
import com.mealfit.common.baseEntity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.DynamicUpdate;

@ToString
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

    @Exclude
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostImage> images = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Post(String content) {
        this.content = content;
        this.likeIt = 0;
        this.view = 0;
    }

    public void settingUserInfo(Long userId, String profileImage, String nickName) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickName = nickName;
    }

    public void addPostImages(List<PostImage> images) {
        for (PostImage postImage : images) {
            addPostImage(postImage);
        }
    }

    public void replacePostImages(List<PostImage> images) {
        this.images.clear();

        for (PostImage postImage : images) {
            addPostImage(postImage);
        }
    }

    private void addPostImage(PostImage postImage) {
        postImage.setPost(this);
        this.images.add(postImage);
    }

    public void updateContent(String content) {
        this.content = content;
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

    @Builder
    public Post(Long id, Long userId, String profileImage, String nickName, String content,
                int view,
                int likeIt, List<PostImage> images) {
        this.id = id;
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.content = content;
        this.view = view;
        this.likeIt = likeIt;
        this.images = images;
    }


    // Post에서 Comment에 대한 정보 넣기.
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}