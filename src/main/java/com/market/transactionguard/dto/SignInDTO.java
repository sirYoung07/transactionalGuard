package com.market.transactionguard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class SignInDTO {
    @NotNull(message = "email filed is required")
    @NotBlank(message = "email field cannot be blank")
    private String email;

    @NotNull(message = "password field is required")
    @NotBlank(message = "password field cannot be blank")
    private String password;

    private String jwt;

    private String message;


    public SignInDTO(String message, String jwt) {
        this.message = message;
        this.jwt = jwt;
    }

}
