package com.seatup.auth.service;

import com.seatup.user.entity.User;
import com.seatup.user.repository.UserRepository;
import com.seatup.auth.dto.LoginRequest;
import com.seatup.auth.dto.SignUpRequest;
import com.seatup.auth.exception.DuplicateLoginIdException;
import com.seatup.auth.exception.LoginFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입을 실행한다.
     * @param request
     */
    public void signUp(SignUpRequest request) {

        // 로그인 아이디 중복 체크
        if (isUserExists(request.getLoginId())) {
            throw new DuplicateLoginIdException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User signUpUser = User.builder()
                .loginId(request.getLoginId())
                .password(encodedPassword)
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();

        try {
            userRepository.save(signUpUser);
        } catch (Exception e) {
            throw new RuntimeException("데이터베이스 오류 발생");
        }
    }

    /**
     * 이미 존재하는 아이디인지 여부를 반환한다.
     * @param loginId
     * @return
     */
    private boolean isUserExists(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * 로그인 처리
     * @param loginRequest
     */
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginFailedException();
        }

        return user;
    }
}
