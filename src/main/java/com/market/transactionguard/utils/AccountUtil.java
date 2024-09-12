package com.market.transactionguard.utils;

import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountUtil {

    @Getter
    @Value("${monnify.contractCode}")
    private String contractCode;

    private final UserRepository userRepository;

    public AccountUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String generateReference (){

        return "ref" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

    }

    public  String generatePaymentReference(){
        return  "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0,16);
    }



    public String generateUniquePaymentReference() {
        return "payment-" + System.currentTimeMillis();
    }

    public String getAuthenticatedUserEmail(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return  (authentication != null) ? authentication.getName(): "Unknown user";
        }catch (NullPointerException e){
            return e.getLocalizedMessage();
        }

    }

    public String generatePaymentLink(){
        // Point to the backend API that will handle Monnify payment initialization
        return "{/api/transactions/";

    }


    public String getCustomerName(String authenticatedUserEmail){

        try{
            Optional<User> user = Optional.ofNullable(userRepository.findByEmail(authenticatedUserEmail).orElseThrow(null));
            return  (user.get().getFirstName() + " " + user.get().getLastName());
        }catch (NullPointerException e){
            return e.getLocalizedMessage();
        }

    }

    public Optional<User> getAuthenticatedUser(){
        return userRepository.findByEmail(getAuthenticatedUserEmail());
    }



}
