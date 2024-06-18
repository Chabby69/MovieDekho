package com.moviedekho.PaymentService.service;

import com.moviedekho.PaymentService.model.PaymentRequest;
import com.moviedekho.PaymentService.model.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {


    PaymentResponse addPayment(PaymentRequest paymentRequest);

    PaymentResponse updatePayment(PaymentRequest paymentRequest);
}
