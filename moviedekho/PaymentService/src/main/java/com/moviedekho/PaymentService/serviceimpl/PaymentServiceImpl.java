package com.moviedekho.PaymentService.serviceimpl;

import com.moviedekho.PaymentService.entity.Payment;
import com.moviedekho.PaymentService.model.PaymentRequest;
import com.moviedekho.PaymentService.model.PaymentResponse;
import com.moviedekho.PaymentService.repo.PaymentRepository;
import com.moviedekho.PaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentResponse addPayment(PaymentRequest paymentRequest) {
        Optional<Payment> userPaymentAlreadyExists =  paymentRepository.findByUsername(paymentRequest.getUsername());

        if (userPaymentAlreadyExists.isEmpty() && isPaymentValid(paymentRequest)) {
            Payment payment = new Payment();
            payment.setUsername(paymentRequest.getUsername());
            payment.setAmount(paymentRequest.getAmount());
            payment.setPaymentDate(new Date());
            payment.setSubscriptionPlan(paymentRequest.getSubscriptionPlan());
            paymentRepository.save(payment);
            PaymentResponse response = new PaymentResponse();
            response.setMessage("Payment Done SuccessFully");
            response.setPaymentDetails(payment);
            return response;
        }else{
            updatePayment(paymentRequest);
        }
        return null;
    }

    @Override
    public PaymentResponse updatePayment(PaymentRequest paymentRequest) {
        Optional<Payment> userPaymentAlreadyExists =  paymentRepository.findByUsername(paymentRequest.getUsername());
        if(userPaymentAlreadyExists.isPresent()){
            Payment userPayment = userPaymentAlreadyExists.get();
            userPayment.setPaymentDate(new Date());
            userPayment.setSubscriptionPlan(paymentRequest.getSubscriptionPlan());
            paymentRepository.save(userPayment);
            PaymentResponse response = new PaymentResponse();
            response.setMessage("Payment Updated SuccessFully");
            response.setPaymentDetails(userPayment);
            return response;
        }
        return null;
    }

    private boolean isPaymentValid(PaymentRequest payment) {
        Optional<Payment> paymentOptional = paymentRepository.findByUsernameAndSubscriptionPlan(payment.getUsername(),
                payment.getSubscriptionPlan());
        return paymentOptional.isEmpty();
    }

}
