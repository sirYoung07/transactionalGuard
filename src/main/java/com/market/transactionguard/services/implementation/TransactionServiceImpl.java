package com.market.transactionguard.services.implementation;

import com.market.transactionguard.dto.request.PaymentInitializationRequest;
import com.market.transactionguard.dto.request.TransactionRequest;
import com.market.transactionguard.dto.response.PaymentInitializationResponse;
import com.market.transactionguard.dto.response.PaymentVerificationResponse;
import com.market.transactionguard.dto.response.TransactionResponse;
import com.market.transactionguard.entities.*;
import com.market.transactionguard.repositories.TransactionRepository;
import com.market.transactionguard.services.TransactionService;
import com.market.transactionguard.utils.AccountUtil;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${monnify.uri}")
    private String monnifyUri;

    @Getter
    @Value("${monnify.contractCode}")
    private String contractCode;

    @Getter
    @Value("${app.base-uri}")
    private String appUri;

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private  final AccountUtil accountUtil;

    private final WebClient webClient;

    private final AccountServiceImpl accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountUtil accountUtil, WebClient webClient, AccountServiceImpl accountService) {
        this.transactionRepository = transactionRepository;
        this.accountUtil = accountUtil;
        this.webClient = WebClient.builder().baseUrl(monnifyUri).build();
        this.accountService = accountService;
    }


    @Override
    public ResponseEntity<String> createATransaction(TransactionRequest transactionRequest, List<MultipartFile> productImages) {

        if (productImages == null  || productImages.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("images are required");
        }

        String uploadDir = "/home/siryoung/Projects/transactionguard/src/main/java/com/market/transactionguard/storage";  // Specify your upload directory
        //TODO : WORK WITH ONLINE CLOUD FOR SAVING FILE

        StringBuilder imageLinks = new StringBuilder();

        Path uploadPath = Paths.get(uploadDir);


        for (MultipartFile file : productImages) {
            try {
                // Save the file to the upload directory
                String fileName = file.getOriginalFilename();
                assert fileName != null;
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                // Build image link string (e.g., could be a local file path or a URL if uploaded to a cloud service)
                String imageLink = filePath.toString();  // This could also be a URL if uploaded to a cloud service
                imageLinks.append(imageLink).append(";");  // Append image link, separated by semicolons

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving the image file: " + e.getMessage());
            }
        }

        var transaction = getTransaction(transactionRequest, imageLinks);

        transactionRepository.save(transaction);

        String paymentLink = accountUtil.generatePaymentLink() + transaction.getId();


        // TODO : SEND EMAIL TO THE RECEIVER WITH THE PAYMENT LINK.


        return  ResponseEntity.ok("Transaction has been successfully initialized" + " "  + paymentLink);
    }


    private Transaction getTransaction(TransactionRequest transactionRequest, StringBuilder imageLinks) {
        Optional<User> authenticatedUser = accountUtil.getAuthenticatedUser();

        Product product = new Product();

        // Set image links as a single string in the Product entity
        product.setProductName(transactionRequest.getProduct().getProductName());
        product.setProductDescription(transactionRequest.getProduct().getProductDescription());
        product.setProductDetails(transactionRequest.getProduct().getProductDetails());
        product.setProductImages(imageLinks.toString());

        Transaction transaction  = new Transaction( );
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setRecipientEmailAddress(transactionRequest.getRecipientEmailAddress());
        transaction.setRecipientPhoneNumber(transactionRequest.getRecipientPhoneNumber());
        transaction.setRecipientName(transactionRequest.getRecipientName());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setProduct(product);

        //Set the relationship between Transactions and User
        User user = authenticatedUser.orElseThrow(()-> new RuntimeException("Authenticated user not found"));
        transaction.setUser(authenticatedUser.get());
        return transaction;
    }


    @Override
    public ResponseEntity<TransactionResponse> getTransactionById(Long transactionId) {

        if(accountUtil.getAuthenticatedUser().isEmpty()){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TransactionResponse("User not authenticated"));
        }

        Optional<Transaction> transactionOptional =  transactionRepository.findById(transactionId);

        if (transactionOptional.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransactionResponse("Transaction not found"));
        }

        if(!Objects.equals(transactionOptional.get().getUser().getId(), accountUtil.getAuthenticatedUser().get().getId())){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(new TransactionResponse("Access denied"));
        }

        TransactionResponse response = new TransactionResponse(
            "success",
            transactionOptional.get().getAmount(),
            transactionOptional.get().getRecipientEmailAddress(),
            transactionOptional.get().getRecipientPhoneNumber(),
            transactionOptional.get().getRecipientName(),
            transactionOptional.get().getTransactionStatus(),
            transactionOptional.get().getProduct()
        );

        return  ResponseEntity.ok(response);

        // TODO: GENERATE A LINK TO REDIRECT RECIPIENT TO THE MAKE PAYMENT



    }


    // THIS METHOD IS USED TO INILIAZE A  TRANSACTION
    @Override
    public ResponseEntity<PaymentInitializationResponse> initializePayment(Long transactionId) {

        if (accountUtil.getAuthenticatedUser().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaymentInitializationResponse("User not authenticated"));
        }

        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (transactionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentInitializationResponse("Transaction not found"));
        }

        if (!Objects.equals(transactionOptional.get().getUser().getId(), accountUtil.getAuthenticatedUser().get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PaymentInitializationResponse("Access denied"));
        }

        ArrayList<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("CARD");
        paymentMethods.add("ACCOUNT_TRANSFER");

        // SET REQUEST TO BE SENT TO MONNIFY
        PaymentInitializationRequest paymentInitializationRequest = new PaymentInitializationRequest();
        paymentInitializationRequest.setAmount(transactionOptional.get().getAmount());
        paymentInitializationRequest.setCustomerName(transactionOptional.get().getRecipientName());
        paymentInitializationRequest.setCustomerEmail(transactionOptional.get().getRecipientEmailAddress());
        paymentInitializationRequest.setPaymentDescription("SERVICE PAYMENT");
        paymentInitializationRequest.setPaymentReference(accountUtil.generatePaymentReference());
        paymentInitializationRequest.setContractCode(contractCode);
        paymentInitializationRequest.setCurrencyCode("NGN");
        paymentInitializationRequest.setPaymentMethods(paymentMethods);
        paymentInitializationRequest.setRedirectUrl(appUri);


        log.info(String.valueOf(paymentInitializationRequest));

        try {

            PaymentInitializationResponse response = webClient.method(HttpMethod.POST)
                .uri(monnifyUri + "/api/v1/merchant/transactions/init-transaction")
                .header("Authorization", "Bearer " + accountService.getAccessTokenFromMonnify())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentInitializationRequest)
                .retrieve().bodyToMono(PaymentInitializationResponse.class).block();
            if (response != null && response.isRequestSuccessful()) {
                return ResponseEntity.ok(response);
            }else {
                throw new RuntimeException("Failed to initiate payment: " +
                    (response != null ? response.getResponseMessage() : "Internal Server error"));
            }


        } catch (WebClientResponseException e) {

            log.error("error while initilizing paymenet a", e);

            return ResponseEntity.status(e.getStatusCode()).body(new PaymentInitializationResponse(e.getStatusCode().toString(), "Failed to initiate payment. Please try again later"));
        } catch (Exception e) {
            log.error("Unexpected error while making payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentInitializationResponse("500", "An internal server error occurred"));
        }

    }

    @Override
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(String paymentReference) {

        try {
            log.info("using monnify url{} ", monnifyUri);

            PaymentVerificationResponse response = webClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(monnifyUri.replace("https://", ""))
                    .path("/api/v2/merchant/transactions/query")
                    .queryParam("paymentReference", paymentReference)
                    .build())
                .header("Authorization", "Bearer " + accountService.getAccessTokenFromMonnify())
                .retrieve().bodyToMono(PaymentVerificationResponse.class).block();

            if (response != null && response.isRequestSuccessful()) {
                log.info("Transaction verified successfully" + paymentReference);
                return ResponseEntity.ok(response);
            }else {
                throw new RuntimeException("Failed to get transaction : " +
                    (response != null ? response.getResponseMessage() : "Internal Server error"));
            }


        } catch (WebClientResponseException e) {

            log.error("error while verifying payment", e);

            return ResponseEntity.status(e.getStatusCode()).body(new PaymentVerificationResponse(e.getStatusCode().toString(), "Failed to get transaction status.  Please try again later"));
        } catch (Exception e) {
            log.error("Unexpected error while making payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentVerificationResponse("500", "An internal server error occurred"));
        }
    }


}
