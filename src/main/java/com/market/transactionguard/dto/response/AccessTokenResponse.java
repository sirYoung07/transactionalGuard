package com.market.transactionguard.dto.response;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AccessTokenResponse {

    private boolean requestSuccessful;
    private String responseMessage;
    private String responseCode;
    private ResponseBody responseBody;

    @Getter
    @Setter
    public static class ResponseBody {
        private String accessToken;
        private int expiresIn;
    }
}
