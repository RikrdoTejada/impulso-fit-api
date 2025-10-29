package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequest;
import com.impulsofit.dto.response.LoginResponse;
import com.impulsofit.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginDTO) {
        return loginService.login(loginDTO);
    }
}
