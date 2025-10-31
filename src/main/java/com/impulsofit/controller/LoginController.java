package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return loginService.login(loginDTO);
    }
}
