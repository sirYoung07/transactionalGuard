package com.market.transactionguard.dto.response;

import com.market.transactionguard.entities.Product;
import com.market.transactionguard.entities.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

        private String message;

        private BigDecimal amount;

        private String recipientEmailAddress;

        private String recipientPhoneNumber;

        private String recipientName;

        private TransactionStatus transactionStatus;


        private com.market.transactionguard.entities.Product product;



    private static class Product {
            private String productName;
            private String productDescription;
            private String  productDetails;
            private String productImage;

    }

    public TransactionResponse(String message) {
        this.message = message;
    }

    public TransactionResponse(BigDecimal amount, String recipientEmailAddress, String recipientPhoneNumber,  String recipientName, TransactionStatus transactionStatus, com.market.transactionguard.entities.Product product) {
        this.amount = amount;
        this.recipientEmailAddress = recipientEmailAddress;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.recipientName = recipientName;
        this.transactionStatus  = transactionStatus;
        this.product = product;
    }
}
