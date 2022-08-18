package com.mealfit.comment.controller;

import com.mealfit.comment.dto.CommentRequestDto;
import com.mealfit.comment.dto.CommentResponseDto;
import com.mealfit.comment.service.CommentService;
import com.mealfit.config.security.details.UserDetailsImpl;
import com.mealfit.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //작성
    @PostMapping("posts/{postId}/comments")
    public ResponseEntity<Void> saveComment(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.createComment(postId, commentRequestDto,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(null);
    }
    //삭제
    @DeleteMapping("/comment")
    public Long deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal
                              User user) {
        return commentService.deleteComment(commentId,user);
    }
    //수정
    @PutMapping("/comment")
    public ResponseEntity<Void> updateComment(@PathVariable Long commentId, CommentRequestDto commentDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.updateComment(commentId, commentDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
    //조회
    @GetMapping("/post/{postId}/comment")
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
