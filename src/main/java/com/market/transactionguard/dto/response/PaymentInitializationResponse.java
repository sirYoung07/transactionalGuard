package com.market.transactionguard.dto.response;

import lombok.*;

import java.util.ArrayList;

@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitializationResponse {
    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private PaymentInitializationResponseBody responseBody;

    public PaymentInitializationResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public PaymentInitializationResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    @Data
    public static class  PaymentInitializationResponseBody{
        private String transactionReference;
        private String paymentReference;
        private String MerchantName;
        private ArrayList<String> enabledPaymentMethod;
        private String checkoutUrl;
    }
}
