package com.market.transactionguard.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonnifyReservedAccountResponse {
    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private MonnifyReservedAccountResponseBody responseBody;

    public MonnifyReservedAccountResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;

    }



    @Data
    public static class MonnifyReservedAccountResponseBody {
        private String contractCode;
        private String accountReference;
        private String accountName;
        private String currencyCode;
        private String customerEmail;
        private String customerName;
        private List<MonnifyAccountDetails> accounts;
        private String collectionChannel;
        private String reservationReference;
        private String reservedAccountType;
        private String status;
        private String createdOn;
        private List<String> incomeSplitConfig;
        private String bvn;
        private String nin;
        private boolean restrictPaymentSource;

    }

    @Data
    public static class MonnifyAccountDetails {
        private String bankCode;
        private String bankName;
        private String accountNumber;
        private String accountName;

        // getters and setters
    }




}
