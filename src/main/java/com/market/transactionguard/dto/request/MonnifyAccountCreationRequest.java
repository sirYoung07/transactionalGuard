package com.market.transactionguard.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonnifyAccountCreationRequest {
    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String contractCode;
    private String customerEmail;
    private String customerName;
    //private String bvn;
    private String nin;
    private Boolean getAllAvailableBanks;



//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class  BvnDetails{
//        private String bvn;
//        private String bvnDateOfBirth;
//
//    }

}
