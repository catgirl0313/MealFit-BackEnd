package com.mealfit.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.loginInfo.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.userProfile.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.userProfile.nickname = ?1")
    Optional<User> findByNickname(String nickname);

    @Query("select (count(u) > 0) from User u where u.loginInfo.username = ?1")
    boolean existsByUsername(String username);

    @Query("select (count(u) > 0) from User u where u.userProfile.email = ?1")
    boolean existsByEmail(String email);

    @Query("select (count(u) > 0) from User u where u.userProfile.nickname = ?1")
    boolean existsByNickname(String nickname);
}
