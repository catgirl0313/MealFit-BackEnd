package com.mealfit.comment.controller;

import com.mealfit.comment.dto.CommentCUDResponseDto;
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


    //작성하기
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentCUDResponseDto> createComment(@PathVariable Long postId,
                                                               @Valid CommentRequestDto requestDto,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentCUDResponseDto responseDto = commentService.createComment(postId,requestDto,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @DeleteMapping("/post/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK)
                .body("삭제 완료!");
    }

    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @Valid CommentRequestDto requestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.updateComment(commentId,userDetails.getUser(),requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("수정 완료!");
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<CommentWrapper<List<CommentResponseDto>>>listcomment(@PathVariable Long postId){
        List<CommentResponseDto> doList = commentService.listComment(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CommentWrapper<>(doList));
    }

    @Data
    @AllArgsConstructor
    static class CommentWrapper<T> {
        private T comments;
    }
}
