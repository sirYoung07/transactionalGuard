package com.market.transactionguard.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class SignUpRequest {
    @Valid
    @NotNull(message = "firstName filed is required")
    @NotBlank(message = "firstName field cannot be blank")
    private String firstName;

    @NotNull(message = "lastName filed is required")
    @NotBlank(message = "LastName field cannot be blank")
    private String lastName;

    @NotNull(message = "email filed is required")
    @NotBlank(message = "email field cannot be blank")
    @Email(message = "invalid email format")
    private String email;

    @NotNull(message = "userName filed is required")
    @NotBlank(message = "The userName field cannot be blank")
    private String userName;


    @NotNull(message = "password filed is required")
    @NotBlank(message = "password field cannot be blank")
    private String password;


}
