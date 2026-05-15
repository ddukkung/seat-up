package com.seatup.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthViewController {

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "auth/sign-up";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

}
