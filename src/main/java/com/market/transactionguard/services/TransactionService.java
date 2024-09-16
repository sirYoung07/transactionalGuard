package com.market.transactionguard.services;

import com.market.transactionguard.dto.request.TransactionRequest;
import com.market.transactionguard.dto.response.PaymentInitializationResponse;
import com.market.transactionguard.dto.response.PaymentVerificationResponse;
import com.market.transactionguard.dto.response.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface TransactionService {
    ResponseEntity<String> createATransaction(TransactionRequest transactionRequest, List<MultipartFile> productImages);

    ResponseEntity<TransactionResponse> getTransactionById(Long transactionId);

    ResponseEntity<PaymentInitializationResponse> initializePayment(Long transactionId);

    ResponseEntity<PaymentVerificationResponse> verifyPayment(String paymentReference);
}
