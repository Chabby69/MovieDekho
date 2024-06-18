package com.moviedekho.userservie.config;


import com.moviedekho.userservie.model.request.PaymentRequest;
import com.moviedekho.userservie.model.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentservice", url = "http://localhost:8083/api/payments")
public interface PaymentServiceClient {

    @PostMapping("/create")
    void createPayment(@RequestBody PaymentRequest paymentRequest);

    @GetMapping("/api/payments/{paymentId}")
    PaymentResponse getPaymentDetails(@PathVariable("paymentId") Long paymentId);
}
