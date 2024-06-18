package com.market.transactionguard.controllers;

import com.market.transactionguard.Exception.GlobalException;
import com.market.transactionguard.dto.request.AccountCreationRequest;
import com.market.transactionguard.dto.response.MonnifyReservedAccountResponse;
import com.market.transactionguard.entities.Account;
import com.market.transactionguard.services.implementation.AccountServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Accounts controller", description = "Account api")
@RestController
@RequestMapping("/api/v1/account-management")
public class AccountController  {
    private final AccountServiceImpl accountService;
    public AccountController(AccountServiceImpl walletService, AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<MonnifyReservedAccountResponse> createAccount(@RequestBody @Valid AccountCreationRequest accountCreationRequest){
        return accountService.createReservedAccount(accountCreationRequest);

    }




    @GetMapping("/reserved/accounts")
    public ResponseEntity<List<Account>> getReservedAccounts(){
        return accountService.getUserReservedAccounts();
    }



    @PostMapping("/get-access-token")
    public String getAccessToken(){
        return accountService.getAccessTokenFromMonnify();
    }
}
