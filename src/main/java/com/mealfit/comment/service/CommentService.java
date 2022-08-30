package com.mealfit.comment.service;

import com.mealfit.comment.domain.Comment;
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

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    //작성하기
    public Long createComment(Long postId, User user,CommentRequestDto requestDto){

        Post post = postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("게시글이 업습니다."));
        requestDto.setPostId(postId);
        Comment commentEntity = requestDto.toEntity();
        commentEntity.settingUserInfo(user.getId(), user.getUserProfile().getProfileImage());
        commentRepository.save(commentEntity);

        return requestDto.getCommentId();
    }

    private static void validateUser(User user,Comment comment) {
        Long commentUserId = comment.getUserId();
        if (!user.getId().equals(commentUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로 수정할 수 없습니다.");
        }
    }
    //삭제하기
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("댓글이 없습니다."));

        validateUser(user, comment);
        commentRepository.deleteById(commentId);
    }
    //수정하기
    public void updateComment(Long commentId, User user, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("댓글이 없습니다."));

        validateUser(user, comment);
        comment.update(requestDto.getComment());
    }

    //댓글 리스트
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
