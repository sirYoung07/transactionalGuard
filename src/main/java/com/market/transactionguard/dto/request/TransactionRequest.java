package com.market.transactionguard.dto.request;

import com.market.transactionguard.entities.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(message = "amount field is required")
    @NotBlank(message = "amount field cannot be blank")
    private BigDecimal amount;

    @NotNull(message = "recipient email field is required")
    @NotBlank(message = "recipient email field cannot be blank")
    private String recipientEmailAddress;

    @NotNull(message = "recipient phone number is required")
    @NotBlank(message = "amount phone number cannot be blank")
    private String recipientPhoneNumber;

    @NotNull(message = "recipient name is required")
    @NotBlank(message = "recipient name cannot be blank")
    private String recipientName;


    private Product product;

    @Data
    public static class  Product{

        @Column(name = "name")
        private String productName;

        @Column(name = "details")
        private String productDetails;

        @Column(name = "description")
        private String productDescription;
    }



}
