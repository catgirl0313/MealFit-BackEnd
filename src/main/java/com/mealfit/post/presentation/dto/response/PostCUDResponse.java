package com.mealfit.post.presentation.dto.response;

import com.mealfit.post.domain.Post;
import com.mealfit.post.dto.WriterDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCUDResponse {

    private Long postId;
    private String nickname;
    private String profileImage;
    WriterDto writerDto;
    private String content;
    private int likeCnt;
    private List<String> imageUrls;

    public PostCUDResponse(Post post) {
        this.postId = post.getId();
        this.nickname = post.getNickname();
        this.profileImage = post.getProfileImage();
        this.content = post.getContent();
        this.likeCnt = post.getLikeIt();
        this.imageUrls = post.getImageUrls();
    }

}

