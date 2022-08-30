package com.mealfit.authentication.domain;

import java.util.Optional;

public interface OAuthTokenDao {

    void insert(JwtToken jwtToken);

    Optional<String> findByKey(String key);
}
