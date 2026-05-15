package com.seatup.user;

import com.seatup.auth.service.AuthService;
import com.seatup.auth.dto.LoginRequest;
import com.seatup.auth.dto.SignUpRequest;
import com.seatup.user.entity.User;
import com.seatup.user.enums.DeleteStatus;
import com.seatup.user.enums.Role;
import com.seatup.auth.exception.DuplicateLoginIdException;
import com.seatup.auth.exception.LoginFailedException;
import com.seatup.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 회원가입_실패_이미_존재하는_아이디() {
        // given
        SignUpRequest member = new SignUpRequest();
        member.setLoginId("test01");
        member.setPassword("test1234!");
        member.setName("test01");
        member.setEmail("test@test.com");

        // then
        assertThrows(DuplicateLoginIdException.class, () -> authService.signUp(member));
    }

    @Test
    void 회원가입_성공() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setLoginId("test02");
        request.setPassword("1234");
        request.setName("test02");
        request.setEmail("test02@test.com");

        // when
        authService.signUp(request);

        // then
        User found = userRepository.findByLoginId(request.getLoginId()).get();

        assertThat(found.getLoginId()).isEqualTo(request.getLoginId());
        assertThat(found.getRole()).isEqualTo(Role.USER);
        assertThat(found.getIsDeleted()).isEqualTo(DeleteStatus.N);
    }

    @Test
    void 로그인_성공() {
        // given
        LoginRequest request = new LoginRequest();
        request.setLoginId("test01");
        request.setPassword("test1234!");

        // when & then
        Assertions.assertDoesNotThrow(() -> authService.login(request));
    }

    @Test
    void 로그인_실패() {
        // given
        LoginRequest request = new LoginRequest();
        request.setLoginId("test01");
        request.setPassword("1234");

        // when & then
        assertThrows(LoginFailedException.class, () -> authService.login(request));
    }
}
