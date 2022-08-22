package com.mealfit.post.dto;



import com.mealfit.post.domain.Post;

import javax.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {

    @NotBlank
    private String content;

    private List<MultipartFile> postImageList;

    public Post toEntity() {
        return new Post(content);
    }

}
