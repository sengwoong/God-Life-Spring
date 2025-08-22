package com.Dongo.GodLife.User.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenStorageService {

    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String ACCESS_TOKEN_PREFIX = "access_token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String USER_TOKENS_PREFIX = "user_tokens:";

    /**
     * 액세스 토큰을 Redis에 저장
     */
    public void storeAccessToken(String userId, String accessToken, Duration expiration) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        String userKey = USER_TOKENS_PREFIX + userId;
        
        // 토큰을 키로 저장
        redisTemplate.opsForValue().set(key, userId, expiration);
        
        // 사용자별 토큰 목록에 추가
        redisTemplate.opsForSet().add(userKey, accessToken);
        redisTemplate.expire(userKey, expiration);
        
        log.debug("Access token stored for user: {}", userId);
    }

    /**
     * 리프레시 토큰을 Redis에 저장
     */
    public void storeRefreshToken(String userId, String refreshToken, Duration expiration) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        String userKey = USER_TOKENS_PREFIX + userId;
        
        // 토큰을 키로 저장
        redisTemplate.opsForValue().set(key, userId, expiration);
        
        // 사용자별 토큰 목록에 추가
        redisTemplate.opsForSet().add(userKey, refreshToken);
        redisTemplate.expire(userKey, expiration);
        
        log.debug("Refresh token stored for user: {}", userId);
    }

    /**
     * 액세스 토큰이 유효한지 확인
     */
    public boolean isAccessTokenValid(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 리프레시 토큰이 유효한지 확인
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 토큰으로 사용자 ID 조회
     */
    public String getUserIdFromToken(String token, boolean isAccessToken) {
        String prefix = isAccessToken ? ACCESS_TOKEN_PREFIX : REFRESH_TOKEN_PREFIX;
        String key = prefix + token;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 액세스 토큰 무효화 (로그아웃)
     */
    public void invalidateAccessToken(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + accessToken;
        String userId = redisTemplate.opsForValue().get(key);
        
        if (userId != null) {
            // 토큰 삭제
            redisTemplate.delete(key);
            
            // 사용자별 토큰 목록에서 제거
            String userKey = USER_TOKENS_PREFIX + userId;
            redisTemplate.opsForSet().remove(userKey, accessToken);
            
            log.debug("Access token invalidated for user: {}", userId);
        }
    }

    /**
     * 리프레시 토큰 무효화
     */
    public void invalidateRefreshToken(String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + refreshToken;
        String userId = redisTemplate.opsForValue().get(key);
        
        if (userId != null) {
            // 토큰 삭제
            redisTemplate.delete(key);
            
            // 사용자별 토큰 목록에서 제거
            String userKey = USER_TOKENS_PREFIX + userId;
            redisTemplate.opsForSet().remove(userKey, refreshToken);
            
            log.debug("Refresh token invalidated for user: {}", userId);
        }
    }

    /**
     * 사용자의 모든 토큰 무효화 (강제 로그아웃)
     */
    public void invalidateAllUserTokens(String userId) {
        String userKey = USER_TOKENS_PREFIX + userId;
        
        // 사용자별 토큰 목록 조회
        var tokens = redisTemplate.opsForSet().members(userKey);
        
        if (tokens != null) {
            // 모든 토큰 삭제
            tokens.forEach(token -> {
                if (token.startsWith("eyJ")) { // JWT 토큰 형식 확인
                    String accessKey = ACCESS_TOKEN_PREFIX + token;
                    String refreshKey = REFRESH_TOKEN_PREFIX + token;
                    
                    redisTemplate.delete(accessKey);
                    redisTemplate.delete(refreshKey);
                }
            });
            
            // 사용자별 토큰 목록 삭제
            redisTemplate.delete(userKey);
            
            log.debug("All tokens invalidated for user: {}", userId);
        }
    }

    /**
     * 토큰 만료 시간 연장
     */
    public void extendTokenExpiration(String token, boolean isAccessToken, Duration newExpiration) {
        String prefix = isAccessToken ? ACCESS_TOKEN_PREFIX : REFRESH_TOKEN_PREFIX;
        String key = prefix + token;
        
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.expire(key, newExpiration);
            log.debug("Token expiration extended for: {}", key);
        }
    }
}
