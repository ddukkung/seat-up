package com.seatup.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * OncePerRequestFilter
     * - 요청당 한 번만 실행되는 필터
     * - JWT 검증 로직은 요청이 올 때마다 실행되기 때문에 모든 요청에 대해 검증을 수행해야 한다.
     * - 하지만 같은 요청에서 여러 번 실행될 필요는 없으므로 해당 필터 사용
     */

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveAccessToken(request);

        // 토큰 유효성 검증 후 실행
        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            // 블랙리스트 체크
            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Long userId = jwtTokenProvider.getUserIdFromAccessToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // 스프링 시큐리티가 이 요청을 로그인된 요청으로 인식하게 됨
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

}
