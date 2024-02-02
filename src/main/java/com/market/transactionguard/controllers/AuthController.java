package com.market.transactionguard.controllers;

import com.market.transactionguard.dto.SignInDTO;
import com.market.transactionguard.dto.SignUpDTO;
import com.market.transactionguard.dto.request.SignInRequest;
import com.market.transactionguard.dto.request.SignUpRequest;
import com.market.transactionguard.dto.response.SignInResponse;
import com.market.transactionguard.dto.response.SignUpResponse;
import com.market.transactionguard.services.AuthServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth APIs")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }


    @PostMapping("/sign-up")
    public SignUpResponse registerUser(@RequestBody @Valid SignUpRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/sign-in")
    public SignInResponse loginUser(@Valid @RequestBody SignInRequest request){
        return authService.loginUser(request);
    }
}
