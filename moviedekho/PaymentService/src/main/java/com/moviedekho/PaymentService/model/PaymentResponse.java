package com.moviedekho.PaymentService.model;

import com.moviedekho.PaymentService.entity.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {

    private String message;

    private Payment paymentDetails;

}
