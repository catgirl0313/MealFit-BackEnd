package com.mealfit.post.repository;


import com.mealfit.post.domain.LikeIt;
import com.mealfit.post.domain.Post;
import com.mealfit.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PostReadRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByIdLessThan(Long lastId, Pageable pageable);


    @Modifying
    @Query("update Post p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

}
