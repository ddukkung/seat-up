package com.seatup.user.entity;

import com.seatup.user.enums.DeleteStatus;
import com.seatup.user.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOGIN_ID", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 50)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "IS_DELETED", nullable = false, length = 1)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static User create(String loginId, String password, String name, String email, String phoneNumber, String address) {
        User user = new User();
        user.loginId = loginId;
        user.password = password;
        user.name = name;
        user.email = email;
        user.phoneNumber = phoneNumber;
        user.address = address;
        user.role = Role.USER;
        user.isDeleted = DeleteStatus.N;
        user.createdAt = LocalDateTime.now();

        return user;
    }

    /**
     * 기존 회원을 탈퇴 상태로 변경한다.
     */
    public void withdraw() {
        this.isDeleted = DeleteStatus.Y;
    }

}
