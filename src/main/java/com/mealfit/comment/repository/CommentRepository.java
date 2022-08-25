package com.mealfit.comment.repository;

import com.mealfit.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    List<Comment> findByUserIdOrderByCreatedAtDesc(Long UserId);

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
