package com.market.transactionguard.controllers;

import com.market.transactionguard.entities.User;
import com.market.transactionguard.services.implementation.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auth", description = "Auth APIs")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userServiceiImpl;

    public UserController(UserServiceImpl userServiceiImpl) {
        this.userServiceiImpl = userServiceiImpl;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsersWithAccountdetails(){
        return userServiceiImpl.getAllUsersWithAccountdetails();
    }



}
