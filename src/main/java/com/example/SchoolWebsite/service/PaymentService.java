package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getStudentPayments(String studentId) {
        return paymentRepository.findFeePaymentsByStudentId(studentId);
    }
}
