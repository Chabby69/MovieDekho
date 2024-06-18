package com.moviedekho.PaymentService.repo;

import com.moviedekho.PaymentService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Optional<Payment> findByUsernameAndSubscriptionPlan(String username, String subscriptionPlan);

    Optional<Payment> findByUsername(String username);
}

