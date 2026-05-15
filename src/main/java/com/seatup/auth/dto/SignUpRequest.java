package com.seatup.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private Long id;

    @NotBlank(message = "로그인 아이디는 필수입니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{4,12}$",
            message = "아이디는 영문과 숫자 4~12자리만 가능합니다."
    )
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[!#$])[a-zA-Z0-9!#$]{8,14}$",
            message = "비밀번호는 8~14자이며 ! # $ 중 하나 이상을 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    @Pattern(
            regexp = "^[가-힣a-zA-Z]{2,30}$",
            message = "이름은 한글 또는 영문 2~30자만 가능합니다."
    )
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(
            regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$",
            message = "올바른 휴대폰 번호 형식이 아닙니다."
    )
    private String phoneNumber;

    private String address;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null || phoneNumber.isBlank()
                ? null
                : phoneNumber;
    }

}
