package com.market.transactionguard.services.implementation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.market.transactionguard.entities.Account;
import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.UserRepository;
import com.market.transactionguard.services.UserService;
import org.hibernate.Hibernate;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService , UserService{


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("invalid user"));
    }


    @Override
    public ResponseEntity<List<User>> getAllUsersWithAccountdetails() {
        List<User> users = userRepository.findAll();

//        TODO -> PAGINATION AND PROPER RETURN RESPONSE

        return ResponseEntity.ok(users);
    }





}
