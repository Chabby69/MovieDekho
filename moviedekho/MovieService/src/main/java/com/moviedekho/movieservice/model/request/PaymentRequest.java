package com.moviedekho.movieservice.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Long userId;
    private double amount;
    private String subscriptionPlan;
}
