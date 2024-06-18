package com.moviedekho.PaymentService.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private double amount;
    private String subscriptionPlan;
    private String username;

}
