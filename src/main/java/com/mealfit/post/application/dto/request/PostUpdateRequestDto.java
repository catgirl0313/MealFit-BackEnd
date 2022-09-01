package com.mealfit.post.application.dto.request;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostUpdateRequestDto {

    private Long postId;

    private Long userId;

    private String content;

    private List<MultipartFile> postImageList;

    @Builder
    public PostUpdateRequestDto(Long postId, Long userId, String content,
          List<MultipartFile> postImageList) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.postImageList = postImageList;
    }
}
