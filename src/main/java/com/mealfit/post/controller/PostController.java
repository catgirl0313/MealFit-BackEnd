package com.mealfit.post.controller;

import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.post.dto.PostCUDResponseDto;
import com.mealfit.post.dto.PostRequestDto;
import com.mealfit.post.service.PostService;
import com.mealfit.user.domain.User;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor

public class PostController {


    private final PostService postService;

    //게시글작성

    @PostMapping("/post")
    public ResponseEntity<PostCUDResponseDto> createPost(@Valid PostRequestDto postDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        PostCUDResponseDto responseDto = postService.createPost(postDto, userDetailsImpl.getUser());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    //게시글 삭제
    @DeleteMapping("/post")
    public Long deletePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        return postService.deletePost(postId, user);
    }

    //게시글 수정
    @PutMapping("/post")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @Valid PostRequestDto postDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.updatePost(postId, postDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }


}
