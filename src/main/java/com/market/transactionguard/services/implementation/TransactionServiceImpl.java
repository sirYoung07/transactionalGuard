package com.market.transactionguard.services.implementation;

import com.market.transactionguard.dto.request.TransactionRequest;
import com.market.transactionguard.dto.response.TransactionResponse;
import com.market.transactionguard.entities.Product;
import com.market.transactionguard.entities.Transaction;
import com.market.transactionguard.entities.TransactionStatus;
import com.market.transactionguard.entities.User;
import com.market.transactionguard.repositories.TransactionRepository;
import com.market.transactionguard.services.TransactionService;
import com.market.transactionguard.utils.AccountUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private  AccountUtil accountUtil;

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

        // TODO : SEND EMAIL TO THE RECEIVER.


        return  ResponseEntity.ok("Transaction has been successfully initialized");
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



    }





}
