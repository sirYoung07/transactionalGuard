package com.market.transactionguard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class SignInResponse {
    private String message;
    private String jwt;
}
