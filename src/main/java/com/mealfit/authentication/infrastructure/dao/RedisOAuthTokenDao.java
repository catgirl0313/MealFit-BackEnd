package com.mealfit.authentication.infrastructure.dao;

import com.mealfit.authentication.domain.JwtToken;
import com.mealfit.authentication.domain.OAuthTokenDao;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisOAuthTokenDao implements OAuthTokenDao {

    private final ValueOperations<String, String> opsForValue;

    public RedisOAuthTokenDao(StringRedisTemplate redisTemplate) {
        this.opsForValue = redisTemplate.opsForValue();
    }

    @Override
    public void insert(JwtToken jwtToken) {
        opsForValue.set(
              jwtToken.getUsername(),
              jwtToken.getToken(),
              jwtToken.getExpiredTime(),
              TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Optional<String> findByKey(String key) {
        return Optional.ofNullable(opsForValue.get(key));
    }
}
