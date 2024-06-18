package com.moviedekho.userservie.model.response;

import com.moviedekho.userservie.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserResponse extends GenericResponse {

    private String email;
    private String mobileNumber;
    private String role;
    private String username;
    private String dateOfBirth;
    private Set<RoleName> roleNames;
    private String gender;
    private String country;
    private String subscriptionPlan;

}
