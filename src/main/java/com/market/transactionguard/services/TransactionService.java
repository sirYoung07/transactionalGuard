package com.market.transactionguard.services;

import com.market.transactionguard.dto.request.TransactionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionService {
    ResponseEntity<String> createATransaction(TransactionRequest transactionRequest, List<MultipartFile> productImages);
}
