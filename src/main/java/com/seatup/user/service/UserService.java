package com.seatup.user.service;

import com.seatup.auth.dto.UserResponse;
import com.seatup.auth.exception.UserNotFoundException;
import com.seatup.common.exception.BusinessException;
import com.seatup.jwt.JwtTokenProvider;
import com.seatup.user.dto.PasswordUpdateRequest;
import com.seatup.user.dto.ProfileUpdateRequest;
import com.seatup.user.entity.User;
import com.seatup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    /**
     * 사용자 고유 아이디로 회원 정보를 조회한다.
     * @param userId
     * @return
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 사용자 고유 아이디로 조회된 회원 정보를 DTO에 담아 리턴한다.
     * @param userId
     * @return
     */
    public UserResponse getUser(Long userId) {
        User user = findById(userId);
        return UserResponse.from(user);
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.update(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAddress()
        );
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void withdraw(Long userId, String token) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.withdraw();
        addToBlackList(token);
    }

    public void addToBlackList(String token) {
        LocalDateTime expiration = jwtProvider.getAccessTokenExpiration(token);
        long ttl = LocalDateTime.now().until(expiration, ChronoUnit.MILLIS);

        if (ttl > 0) {
            redisTemplate.opsForValue().set(
                    "blacklist:" + token,
                    "logout",
                    ttl,
                    TimeUnit.MILLISECONDS
            );
        }
    }
}
