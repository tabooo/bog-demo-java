package com.bog.demo.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDto {

    private Integer id;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "FirstName is mandatory")
    private String firstName;

    @NotBlank(message = "LastName is mandatory")
    private String lastName;

    @NotBlank(message = "AccountNumber is mandatory")
    private String accountNumber;

    @NotBlank(message = "PersonalNo is mandatory")
    private String personalNo;

    private Integer enabled;
    private Integer state;
    private Double balance;
}