package com.Dongo.GodLife.User.Service;

import com.Dongo.GodLife.User.Dto.LoginRequest;
import com.Dongo.GodLife.User.Dto.LoginResponse;
import com.Dongo.GodLife.User.Dto.RefreshTokenRequest;
import com.Dongo.GodLife.User.Dto.RefreshTokenResponse;
import com.Dongo.GodLife.User.Model.JwtToken;
import com.Dongo.GodLife.User.Model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 처리
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // 사용자 인증
        User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        
        // JWT 토큰 생성
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        // JWT 토큰 정보 생성
        JwtToken jwtToken = JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L) // 15분
                .build();
        
        return LoginResponse.builder()
                .user(user)
                .jwtToken(jwtToken)
                .build();
    }

    /**
     * 액세스 토큰 갱신
     */
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        
        // 리프레시 토큰 유효성 검증
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        // 리프레시 토큰에서 사용자 정보 추출
        Long userId = jwtService.extractUserId(refreshToken);
        User user = userService.findById(userId);
        
        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtService.generateAccessToken(user);
        
        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();
    }

    /**
     * 로그아웃 처리
     */
    public void logout(String accessToken) {
        // 토큰 무효화
        jwtService.invalidateToken(accessToken, true);
        log.info("User logged out successfully");
    }

    /**
     * 강제 로그아웃 (관리자용)
     */
    public void forceLogout(String userId) {
        jwtService.invalidateAllUserTokens(userId);
        log.info("All tokens invalidated for user: {}", userId);
    }

    // JWT 필터에서 필요한 메서드들
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            return jwtService.isAccessTokenValid(token);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * JWT 토큰에서 사용자 이메일 추출
     */
    public String getUserEmailFromToken(String token) {
        try {
            return jwtService.extractEmail(token);
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }
}
