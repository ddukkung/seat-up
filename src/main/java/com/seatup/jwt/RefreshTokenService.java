package com.seatup.jwt;

import com.seatup.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Refresh Token을 저장한다.
     * @param userId
     * @param token
     * @param refreshTokenExpiration
     */
    @Transactional
    public void save(Long userId, String token, Role role, LocalDateTime refreshTokenExpiration) {
        refreshTokenRepository.deleteByUserId(userId);
        RefreshToken refreshToken = RefreshToken.create(userId, token, role, refreshTokenExpiration);
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }
}
