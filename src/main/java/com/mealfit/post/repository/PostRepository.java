package com.mealfit.post.repository;

import com.mealfit.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository  extends JpaRepository<Post, Long> {
}
