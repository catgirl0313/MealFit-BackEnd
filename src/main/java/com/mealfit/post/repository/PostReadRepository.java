package com.mealfit.post.repository;


import com.mealfit.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostReadRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByIdLessThan(Long lastId, Pageable pageable);
}
