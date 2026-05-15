package com.seatup.user.contoller;

import com.seatup.auth.dto.UserResponse;
import com.seatup.jwt.JwtTokenProvider;
import com.seatup.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserApiController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

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

}
