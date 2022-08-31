package com.mealfit.post.repository;

import com.mealfit.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository  extends JpaRepository<Post, Long> {

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsById(Long postId);
}
