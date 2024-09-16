package com.market.transactionguard.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentInitializationRequest {
    private BigDecimal amount;
    private String customerName;
    private String customerEmail;
    private String paymentReference;
    private String paymentDescription;
    private String currencyCode;
    private String contractCode;
    private String redirectUrl;
    private ArrayList<String> paymentMethods;

}
