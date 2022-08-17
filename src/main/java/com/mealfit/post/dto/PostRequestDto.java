package com.mealfit.post.dto;



import com.mealfit.post.domain.Post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {

    private String content;
    private List<MultipartFile> imageUrls;

    public Post toEntity() {
        return new Post(content);

    }

}
