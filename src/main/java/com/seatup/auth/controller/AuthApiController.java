package com.seatup.auth.controller;

import com.seatup.auth.service.AuthService;
import com.seatup.auth.dto.LoginRequest;
import com.seatup.auth.dto.LoginResponse;
import com.seatup.auth.dto.SignUpRequest;
import com.seatup.auth.dto.TokenResponse;
import com.seatup.auth.exception.UnauthorizedException;
import com.seatup.jwt.JwtTokenProvider;
import com.seatup.jwt.RefreshToken;
import com.seatup.jwt.RefreshTokenService;
import com.seatup.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthApiController(JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        User user = authService.login(loginRequest);

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        LocalDateTime refreshExpiry = jwtTokenProvider.getRefreshTokenExpiration(refreshToken);

        // refresh token DB 저장
        refreshTokenService.save(user.getId(), refreshToken, user.getRole(), refreshExpiry);

        // 뷰 페이지 접근을 위해 쿠키에도 토큰 저장
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .body(new LoginResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtTokenProvider.getUserIdFromRefreshToken(refreshToken);

        RefreshToken saved = refreshTokenService.findByUserId(userId)
                .orElseThrow(UnauthorizedException::new);

        if (!saved.getToken().equals(refreshToken)) {
            throw new UnauthorizedException();
        }

        // 새 access token 발급
        String newAccess = jwtTokenProvider.createAccessToken(userId, saved.getRole().name());

        return ResponseEntity.ok(new TokenResponse(newAccess));
    }

}
