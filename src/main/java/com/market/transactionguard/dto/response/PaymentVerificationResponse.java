package com.market.transactionguard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerificationResponse {
    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private PaymentVerificationResponseBody responseBody;

    public PaymentVerificationResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public PaymentVerificationResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    @Data
    public static class  PaymentVerificationResponseBody{
        private String transactionReference;
        private String paymentReference;
        private String amountPaid;
        private String totalPayable;
        private String settlementAmount;
        private String paidOn;
        private String paymentStatus;
        private String paymentDescription;
        private String currency;
        private String paymentMethod;


       // private ArrayList<String> enabledPaymentMethod;
        private String checkoutUrl;
    }
}
