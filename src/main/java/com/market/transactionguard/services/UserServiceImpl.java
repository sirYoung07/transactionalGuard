package com.market.transactionguard.services;

import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("in user service details");
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("invalid user"));
    }

    public Optional<User> getUserByUserId(Long userId){
        return  userRepository.findById(userId);
    }

}
