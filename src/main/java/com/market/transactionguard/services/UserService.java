package com.market.transactionguard.services;

import com.market.transactionguard.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<List<User>> getAllUsersWithAccountdetails();
}
