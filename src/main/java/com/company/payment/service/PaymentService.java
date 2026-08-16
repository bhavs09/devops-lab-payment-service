package com.company.payment.service;

import com.company.payment.model.Payment;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private final AtomicLong idGenerator = new AtomicLong(1000);

    public Payment createPayment(String customerName, double amount) {

        Long id = idGenerator.incrementAndGet();

        return new Payment(
                id,
                customerName,
                amount,
                "SUCCESS"
        );
    }

    public Payment getPayment(Long id) {

        return new Payment(
                id,
                "Demo Customer",
                1000.00,
                "SUCCESS"
        );
    }
}
