package com.mealfit.post.controller;


import com.mealfit.post.dto.PostResponseDto;
import com.mealfit.post.dto.PostsResponseDto;
import com.mealfit.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PostListController {
    private final PostReadService postReadService;

    public static final int DEFAULT_PAGE_SIZE = 12;
// 전체 리스트 조회

    @GetMapping("/post")
    // sort = "id", direction = Sort.Direction.DESC 아이디로 내림차순 정렬
    public ResponseEntity<Slice<PostsResponseDto>> readAll(
            @RequestParam(defaultValue = "" + Long.MAX_VALUE) Long lastId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(postReadService.getReadAll( pageable, lastId));
    }



//상세페이지 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> readOne(@PathVariable Long postId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(postReadService.getReadOne(postId));
    }
//
//    @PostMapping("/post/{postId}/likeIt")
//    public void likes(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        postReadService.likes(postId, userDetails.getUser().getId());
//    }
//    @DeleteMapping("/post/{postId}/likeIt")
//    public void unlikes(@PathVariable Long postId,@AuthenticationPrincipal UserDetailsImpl userDetails){
//        postReadService.unlikes(postId, userDetails.getUser().getId());
//    }


}

