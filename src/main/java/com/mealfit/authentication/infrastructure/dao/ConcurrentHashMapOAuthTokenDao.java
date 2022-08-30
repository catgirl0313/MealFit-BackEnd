package com.mealfit.authentication.infrastructure.dao;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.OAuthTokenDao;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!prod")
@Component
public class ConcurrentHashMapOAuthTokenDao implements OAuthTokenDao {

    private final ConcurrentHashMap<String, String> storage;

    public ConcurrentHashMapOAuthTokenDao() {
        this(new ConcurrentHashMap<>());
    }

    private ConcurrentHashMapOAuthTokenDao(ConcurrentHashMap<String, String> storage) {
        this.storage = storage;
    }

    public void insert(JwtToken jwtToken) {
        storage.put(jwtToken.getUsername(), jwtToken.getToken());
    }

    @Override
    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(storage.get(key));
    }
}
