package com.exercises.crud.controller;

import com.exercises.crud.dto.LoginRequest;
import com.exercises.crud.dto.LoginResponse;
import com.exercises.crud.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = service.login(request);

        return new LoginResponse(token);
    }
}
