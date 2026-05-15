package com.seatup.user;

import com.seatup.user.entity.User;
import com.seatup.user.enums.DeleteStatus;
import com.seatup.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 가입을 테스트한다.")
    void save() {
        User user = User.create(
                "test01",
                "1234",
                "test01",
                "test@test.com",
                null,
                null
        );
        User savedUser = userRepository.save(user);

        assertThat(user.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("회원 조회를 테스트한다.")
    void findById() {
        User user = User.create(
                "test01",
                "1234",
                "test01",
                "test@test.com",
                null,
                null
        );
        User savedUser = userRepository.save(user);

        Optional<User> found = userRepository.findById(savedUser.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getLoginId()).isEqualTo("test01");

    }

    @Test
    @DisplayName("회원 탈퇴를 테스트한다.")
    void delete() {
        User user = User.create(
                "test01",
                "1234",
                "test01",
                "test@test.com",
                null,
                null
        );
        User savedUser = userRepository.save(user);

        savedUser.withdraw();
        userRepository.flush(); // 변경 감지 강제

        User found = userRepository.findById(savedUser.getId()).get();
        assertThat(found.getIsDeleted()).isEqualTo(DeleteStatus.Y);
    }
}