package com.mealfit.comment.application.dto;

import com.mealfit.comment.application.dto.request.CreateCommentRequestDto;
import com.mealfit.comment.application.dto.request.UpdateCommentRequestDto;
import com.mealfit.comment.presentation.dto.request.CreateCommentRequest;
import com.mealfit.comment.presentation.dto.request.UpdateCommentRequest;
import com.mealfit.user.domain.User;

public class CommentServiceDtoFactory {

    public static CreateCommentRequestDto createCommentRequestDto(Long postId,
          User user,
          CreateCommentRequest request) {
        return new CreateCommentRequestDto(
              request.getContent(),
              postId,
              user.getId(),
              user.getUserProfile().getNickname(),
              user.getUserProfile().getProfileImage());
    }

    public static UpdateCommentRequestDto updateCommentRequestDto(Long commentId,
          User user,
          UpdateCommentRequest request) {
        return new UpdateCommentRequestDto(request.getContent(),
              commentId,
              user.getId());
    }
}
