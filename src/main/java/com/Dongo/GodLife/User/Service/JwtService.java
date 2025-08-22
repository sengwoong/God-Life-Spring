package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final TokenStorageService tokenStorageService;

    @Value("${jwt.secret:defaultSecretKey}")
    private String secret;

    @Value("${jwt.access-token.expiration:900000}") // 15분
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration:604800000}") // 7일
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw e;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid: {}", e.getMessage());
            throw e;
        }
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", "USER");
        
        String token = createToken(claims, user.getEmail(), accessTokenExpiration);
        
        // Redis에 토큰 저장
        Duration expiration = Duration.ofMillis(accessTokenExpiration);
        tokenStorageService.storeAccessToken(user.getId().toString(), token, expiration);
        
        return token;
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("type", "refresh");
        
        String token = createToken(claims, user.getEmail(), refreshTokenExpiration);
        
        // Redis에 토큰 저장
        Duration expiration = Duration.ofMillis(refreshTokenExpiration);
        tokenStorageService.storeRefreshToken(user.getId().toString(), token, expiration);
        
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        boolean isValid = (username.equals(extractedUsername) && !isTokenExpired(token));
        
        // Redis에서 토큰 유효성 추가 확인
        if (isValid) {
            isValid = tokenStorageService.isAccessTokenValid(token);
        }
        
        return isValid;
    }

    public Boolean isRefreshTokenValid(String refreshToken) {
        return tokenStorageService.isRefreshTokenValid(refreshToken);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 토큰 무효화 (로그아웃)
     */
    public void invalidateToken(String token, boolean isAccessToken) {
        if (isAccessToken) {
            tokenStorageService.invalidateAccessToken(token);
        } else {
            tokenStorageService.invalidateRefreshToken(token);
        }
    }

    /**
     * 사용자의 모든 토큰 무효화
     */
    public void invalidateAllUserTokens(String userId) {
        tokenStorageService.invalidateAllUserTokens(userId);
    }
}
