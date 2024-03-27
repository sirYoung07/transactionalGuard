package com.market.transactionguard.controllers;

import com.market.transactionguard.services.implementation.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth APIs")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userServiceiImpl;

    public UserController(UserServiceImpl userServiceiImpl) {
        this.userServiceiImpl = userServiceiImpl;
    }

//    @GetMapping("/authenticated")
//    public String registerUser(){
//        return userServiceiImpl.getAuthenticatedUser();
//    }
}
