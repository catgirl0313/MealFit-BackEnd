package com.mealfit.comment.service;

import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.dto.CommentCUDResponseDto;
import com.mealfit.comment.dto.CommentRequestDto;
import com.mealfit.comment.dto.CommentResponseDto;
import com.mealfit.comment.repository.CommentRepository;
import com.mealfit.post.domain.Post;
import com.mealfit.post.repository.PostRepository;
import com.mealfit.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;



    //작성
    @Transactional
    public void createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Post post= postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateCommentDto(commentRequestDto);
        Comment commentEntity = commentRequestDto.toEntity();
        commentEntity.settingUserInfo(user.getId(), user.getProfileImage());



    }


    private void validateCommentDto(CommentRequestDto commentRequestDto) {
        validateContent(commentRequestDto);
    }

    private void validateContent(CommentRequestDto dto) {
        if (dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 넣어주세요.");
        }
    }


    //삭제
    public Long deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다."));

        validateUser(user,comment);
        postRepository.deleteById(commentId);

        return commentId;
    }
    private static void validateUser(User user,Comment comment) {
        Long commentUserId = comment.getUserId();
        if (!user.getId().equals(commentUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //수정
    public void updateComment(Long commentId, CommentRequestDto commentDto, User user) {
        Comment comment  = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateUser(user, comment);

        comment.update(commentDto);
    }
    //조회
    @Transactional
    public List<CommentResponseDto> listComment(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }



}
