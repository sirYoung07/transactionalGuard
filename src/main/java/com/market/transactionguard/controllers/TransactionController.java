package com.market.transactionguard.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.transactionguard.dto.request.TransactionRequest;
import com.market.transactionguard.dto.response.TransactionResponse;
import com.market.transactionguard.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createATransaction(@RequestPart("transactionRequest") String transactionRequest,
                                                     @RequestPart("productImages") List<MultipartFile> productImages) throws Exception {

        // DESERIALIZING THE JSON STRING TO TransactionRequest object
        ObjectMapper objectMapper = new ObjectMapper();
        TransactionRequest transactionRequest1 = objectMapper.readValue(transactionRequest, TransactionRequest.class);

        return transactionService.createATransaction(transactionRequest1,productImages);

    }

    @GetMapping(path = "/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        return transactionService.getTransactionById(transactionId);
    }




}
