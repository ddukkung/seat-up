package com.seatup.auth.dto;

import com.seatup.user.entity.User;
import com.seatup.user.enums.DeleteStatus;
import com.seatup.user.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String loginId;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private Role role;

    private DeleteStatus isDeleted;

    private LocalDateTime createdAt;

    public UserResponse(Long id, String loginId, String name, String email, String phoneNumber, String address, Role role, DeleteStatus isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRole(),
                user.getIsDeleted(),
                user.getCreatedAt()
        );
    }

}