package com.moviedekho.userservie.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Long id;
    private double amount;
    private String subscriptionPlan;
    private String username;

}
