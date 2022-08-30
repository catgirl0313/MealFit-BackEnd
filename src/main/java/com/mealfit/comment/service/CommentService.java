package com.mealfit.comment.service;

import com.mealfit.comment.domain.Comment;
import com.mealfit.comment.dto.CommentCUDResponseDto;
import com.mealfit.comment.dto.CommentRequestDto;
import com.mealfit.comment.dto.CommentResponseDto;
import com.mealfit.comment.dto.UserDto;
import com.mealfit.comment.repository.CommentRepository;

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


    //작성하기
    public CommentCUDResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {


        Comment commentEntity = requestDto.toEntity();
        commentEntity.settingUserInfo(user.getId(), user.getUserProfile().getProfileImage(),user.getUserProfile().getNickname());

        commentRepository.save(commentEntity);
        return new CommentCUDResponseDto(commentEntity,user);

    }

    //삭제하기
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다."));

        validateUser(user, comment);

        commentRepository.deleteById(commentId);

    }

    //수정하기
    public void updateComment(Long commentId, User user, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 댓글이 없습니다."));

        validateUser(user, comment);

        comment.update(requestDto.getComment());
    }

    //작성자 검사
    private static void validateUser(User user, Comment comment) {
        Long commentUserId = comment.getUserId();
        if (!user.getId().equals(commentUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }


    public List<CommentResponseDto> listComment(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment.getId(), comment.getPostId(), comment.getComment(),new UserDto(comment.getNickName(), comment.getProfileImage()),comment.getLikeIt());
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }
}
