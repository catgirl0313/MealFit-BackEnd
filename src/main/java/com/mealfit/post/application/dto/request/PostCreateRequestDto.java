package com.mealfit.post.application.dto.request;


import com.mealfit.post.domain.Post;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostCreateRequestDto {

    private Long postId;

    private Long userId;

    private String nickname;

    private String profileImage;

    private String content;

    private List<MultipartFile> postImageList;

    @Builder
    public PostCreateRequestDto(Long postId, Long userId, String nickname, String profileImage,
          String content, List<MultipartFile> postImageList) {
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.postImageList = postImageList;
    }

    public Post toEntity() {
        Post post = new Post(content);
        post.settingUserInfo(userId, profileImage, nickname);
        return post;
    }
}
