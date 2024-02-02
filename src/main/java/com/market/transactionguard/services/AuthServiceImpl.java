package com.market.transactionguard.services;

import com.market.transactionguard.dto.SignInDTO;
import com.market.transactionguard.dto.SignUpDTO;
import com.market.transactionguard.dto.request.SignInRequest;
import com.market.transactionguard.dto.request.SignUpRequest;
import com.market.transactionguard.dto.response.SignInResponse;
import com.market.transactionguard.dto.response.SignUpResponse;
import com.market.transactionguard.entities.Role;
import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.RoleRepository;
import com.market.transactionguard.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Override
    public SignUpResponse registerUser(SignUpRequest request) {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            return new SignUpResponse("email already exist", null);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getUserName(),
            encodedPassword,
            LocalDateTime.now(),
            LocalDateTime.now(),
            true,
            true,
            true,
            true
        );

        Role userRole = roleRepository.findByAuthority("USER");
        newUser.getRoles().add(userRole);

        userRepository.save(newUser);

        return new SignUpResponse("User registration completed successfully",
            new User(request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getUserName())
        );
    }


    @Override
    public SignInResponse loginUser(SignInRequest request) {


        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if(existingUser.isEmpty()){
            return new SignInResponse("Incorrect email", "null");
        }

        User user = existingUser.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return  new SignInResponse("Incorrect Password", "null");
        }

        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = tokenService.generateJwt(auth);

            return new SignInResponse("login successful", token);


        } catch(AuthenticationException e){
            return new SignInResponse(e.getLocalizedMessage(), null);
        }

    }
}
