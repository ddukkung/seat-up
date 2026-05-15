package com.seatup.user.service;

import com.seatup.auth.dto.UserResponse;
import com.seatup.auth.exception.UserNotFoundException;
import com.seatup.user.entity.User;
import com.seatup.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 로그인 아이디를 통해 회원을 조회한다.
     * @param loginId
     * @return
     */
    public UserResponse getUserByLoginId(String loginId) {
        Optional<User> foundMember = userRepository.findByLoginId(loginId);

        if (foundMember.isEmpty()) {
            throw new UserNotFoundException();
        }

        return UserResponse.from(foundMember.get());
    }

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
}
