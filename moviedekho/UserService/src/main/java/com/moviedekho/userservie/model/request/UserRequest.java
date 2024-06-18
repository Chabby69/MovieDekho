package com.moviedekho.userservie.model.request;

import com.moviedekho.userservie.enums.RoleName;
import com.moviedekho.userservie.enums.SubscriptionPlan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequest {

    private String email;
    private String mobileNumber;
    private RoleName roleName;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private SubscriptionPlan subscriptionPlan;
}
