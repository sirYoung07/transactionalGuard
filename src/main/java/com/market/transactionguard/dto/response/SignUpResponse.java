package com.market.transactionguard.dto.response;

import com.market.transactionguard.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
public class SignUpResponse {

    private String message;
    private User user;

    public SignUpResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }
}
