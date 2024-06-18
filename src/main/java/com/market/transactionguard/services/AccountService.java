package com.market.transactionguard.services;

import com.market.transactionguard.dto.request.AccountCreationRequest;
import com.market.transactionguard.dto.response.MonnifyReservedAccountResponse;
import com.market.transactionguard.entities.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public interface AccountService {
    ResponseEntity<MonnifyReservedAccountResponse> createReservedAccount(AccountCreationRequest accountCreationRequest);

    ResponseEntity<List<Account>> getUserReservedAccounts ();

}
