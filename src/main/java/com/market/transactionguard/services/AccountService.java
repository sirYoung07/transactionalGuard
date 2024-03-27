package com.market.transactionguard.services;

import com.market.transactionguard.dto.request.AccountCreationRequest;
import com.market.transactionguard.dto.response.MonnifyReservedAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    ResponseEntity<MonnifyReservedAccountResponse> createReservedAccount(AccountCreationRequest accountCreationRequest);

}
