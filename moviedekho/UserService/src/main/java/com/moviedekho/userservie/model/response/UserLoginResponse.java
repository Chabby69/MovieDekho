package com.moviedekho.userservie.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moviedekho.userservie.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse extends GenericResponse {

    private String token;
    private String email;
    private String mobileNumber;
    private Set<RoleName> roleNames;
    private String username;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private String subscriptionPlan;
    private String type = "Bearer ";
}
