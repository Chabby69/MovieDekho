package com.moviedekho.movieservice.model.request;

import com.moviedekho.userservie.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserDetail {

    private String email;
    private String mobileNumber;
    private Set<RoleName> roleName;
    private String username;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
}
