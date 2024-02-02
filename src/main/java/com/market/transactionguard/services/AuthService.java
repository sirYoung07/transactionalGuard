package com.market.transactionguard.services;

import com.market.transactionguard.dto.SignInDTO;
import com.market.transactionguard.dto.SignUpDTO;
import com.market.transactionguard.dto.request.SignInRequest;
import com.market.transactionguard.dto.request.SignUpRequest;
import com.market.transactionguard.dto.response.SignInResponse;
import com.market.transactionguard.dto.response.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    SignUpResponse registerUser(SignUpRequest request);

    SignInResponse loginUser(SignInRequest request);

}


