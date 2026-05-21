package com.seatup.user.contoller;

import com.seatup.auth.dto.UserResponse;
import com.seatup.jwt.JwtTokenProvider;
import com.seatup.jwt.UserPrincipal;
import com.seatup.user.dto.PasswordUpdateRequest;
import com.seatup.user.dto.ProfileUpdateRequest;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> verifyToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = jwtTokenProvider.getUserIdFromAccessToken(token);
        UserResponse user = userService.getUser(userId);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody @Valid ProfileUpdateRequest request) {
        userService.updateProfile(principal.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserPrincipal principal,
                                               @RequestBody @Valid PasswordUpdateRequest request) {
        userService.updatePassword(principal.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtTokenProvider.getUserIdFromAccessToken(token);
        userService.withdraw(userId, token);

        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }
}
