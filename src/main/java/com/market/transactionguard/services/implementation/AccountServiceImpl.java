package com.market.transactionguard.services.implementation;

import com.market.transactionguard.config.MonnifyWebClientConfig;
import com.market.transactionguard.dto.request.*;
import com.market.transactionguard.dto.response.AccessTokenResponse;
import com.market.transactionguard.dto.response.ErrorResponse;
import com.market.transactionguard.dto.response.MonnifyReservedAccountResponse;
import com.market.transactionguard.entities.Account;
import com.market.transactionguard.entities.AccountDetails;
import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.AccountDetailsRepository;
import com.market.transactionguard.repositories.AccountRepository;
import com.market.transactionguard.repositories.UserRepository;
import com.market.transactionguard.services.AccountService;
import com.market.transactionguard.utils.Base64Format;
import com.market.transactionguard.utils.AccountUtil;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
*
*/
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
   private final MonnifyWebClientConfig monnifyWebClientConfig;
   private final Base64Format base64Format;
   private final AccountUtil accountUtil;
   private final WebClient webClient;
   private final UserRepository userRepository;
   private final AccountRepository accountRepository;
   private final AccountDetailsRepository accountDetailsRepository;

   @Value("${monnify.uri}")
   private String baseUri;

    @Getter
    @Value("${monnify.contractCode}")
    private String contractCode;



   Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(MonnifyWebClientConfig monnifyWebClientConfig, Base64Format base64Format, AccountUtil walletUtil, AccountUtil accountUtil, WebClient webClient, UserRepository userRepository, AccountRepository accountRepository, AccountDetailsRepository accountDetailsRepository) {
        this.monnifyWebClientConfig = monnifyWebClientConfig;
        this.base64Format = base64Format;
        this.accountUtil = accountUtil;
        this.webClient = webClient;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountDetailsRepository = accountDetailsRepository;
    }




    //CREATE RESERVED ACCOUNT
   @Override
    public ResponseEntity<MonnifyReservedAccountResponse> createReservedAccount(AccountCreationRequest accountCreationRequest) {



           String accountReference = accountUtil.generateReference();

           MonnifyAccountCreationRequest monnifyAccountCreationRequest = new MonnifyAccountCreationRequest();

           monnifyAccountCreationRequest.setAccountReference(accountReference);
           monnifyAccountCreationRequest.setAccountName(accountUtil.getCustomerName(accountUtil.getAuthenticatedUserEmail()));
           monnifyAccountCreationRequest.setCurrencyCode("NGN");
           monnifyAccountCreationRequest.setContractCode(contractCode);
           monnifyAccountCreationRequest.setCustomerEmail(accountUtil.getAuthenticatedUserEmail());
           monnifyAccountCreationRequest.setCustomerName(accountUtil.getCustomerName(accountUtil.getAuthenticatedUserEmail()));
           monnifyAccountCreationRequest.setNin(accountCreationRequest.getNin());
           monnifyAccountCreationRequest.setGetAllAvailableBanks(true);

           logger.info(String.valueOf(monnifyAccountCreationRequest));

       Optional<User> authenticatedUser = accountUtil.getAuthenticatedUser();

       if (authenticatedUser.isPresent() && authenticatedUser.get().getAccount() != null) {

           return ResponseEntity.badRequest().body(new MonnifyReservedAccountResponse("400","User already has an account"));

       }


       try{

                MonnifyReservedAccountResponse response =  webClient.method(HttpMethod.POST)
                   .uri(baseUri + "/api/v2/bank-transfer/reserved-accounts")
                   .header("Authorization", "Bearer " + getAccessTokenFromMonnify())
                   .contentType(MediaType.APPLICATION_JSON)
                   .bodyValue(monnifyAccountCreationRequest)
                   .retrieve().bodyToMono(MonnifyReservedAccountResponse.class).block();

               if(response != null && response.isRequestSuccessful()){

                   //SAVE ACCOUNT DETAILS
                   Account account = new Account();
                   account.setAccountReference(response.getResponseBody().getAccountReference());
                   account.setAccountBalance(BigDecimal.ZERO);

                   List<AccountDetails> accountDetailsList = new ArrayList<>();

                   for(MonnifyReservedAccountResponse.MonnifyAccountDetails accountDetails1 : response.getResponseBody().getAccounts()){
                       AccountDetails newAccountDetails = new AccountDetails();
                       newAccountDetails.setBankCode(accountDetails1.getBankCode());
                       newAccountDetails.setBankName(accountDetails1.getBankName());
                       newAccountDetails.setAccountName(accountDetails1.getAccountName());
                       newAccountDetails.setAccountNumber(accountDetails1.getAccountNumber());

                       newAccountDetails.setAccount(account);
                       accountDetailsList.add(newAccountDetails);

                       accountDetailsRepository.save(newAccountDetails);


                   }



                   //set relationship between user and account
                   account.setUser(authenticatedUser.get());

                    // set relationship between account and account     details
                   account.setAccountDetails(accountDetailsList);

                   accountRepository.save(account);

                   return ResponseEntity.ok(response);

               }else {
                   throw new RuntimeException("Failed to create a reserved account: " +
                       (response != null ? response.getResponseMessage() : "Internal Server error"));
               }


       } catch (WebClientResponseException e) {

           logger.error("error while creating a reserved account", e);

            return  ResponseEntity.status(e.getStatusCode()).body(new MonnifyReservedAccountResponse(e.getStatusCode().toString(), "Failed to create a reserved account. Please try again later"));
       }catch (Exception e){
           logger.error("Unexpected error while creating a reserved account", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MonnifyReservedAccountResponse( "500", "An internal server error occurred"));
       }
   }



    public String getAccessTokenFromMonnify() {
        try {
            AccessTokenResponse response = webClient.method(HttpMethod.POST)
                .uri(baseUri + "/api/v1/auth/login")
                .header("Authorization", "Basic " + base64Format.returnEncodeCredentials())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block(); // Blocking here to get the result

            if (response != null && response.isRequestSuccessful()) {
                return response.getResponseBody().getAccessToken();
            } else {
                // Handle other errors, you can throw an exception, log, or return a default value
                throw new RuntimeException("Failed to retrieve access token: " +
                    (response != null ? response.getResponseMessage() : "Internal Server"));
            }
        } catch (Exception e) {
            // Handle exceptions if any
            logger.error("Error while getting access token", e);
            return  ("Error while getting access token ");
        }
    }



}










