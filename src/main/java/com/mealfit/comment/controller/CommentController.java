package com.mealfit.comment.controller;

import com.mealfit.comment.dto.CommentRequestDto;
import com.mealfit.comment.dto.CommentResponseDto;
import com.mealfit.comment.service.CommentService;
import com.mealfit.config.security.details.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                                        @Valid CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.createComment(postId, userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("작성 완료!");

    }

    @DeleteMapping("/post/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body("삭제완료");
    }

    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @Valid CommentRequestDto requestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.updateComment(commentId,userDetails.getUser(),requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("수정 완료!");
    }

    @GetMapping("post/{postId}/comment")
    public ResponseEntity<CommentWrapper<List<CommentResponseDto>>> listComment(@PathVariable Long postId) {
        List<CommentResponseDto> dtoList = commentService.listComment(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentWrapper<>(dtoList));
    }

    @Data
    @AllArgsConstructor
    static class CommentWrapper<T> {
        private T comments;
    }

}
