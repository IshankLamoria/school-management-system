package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getStudentPayments(String studentId, String startDate, String endDate) {
        return paymentRepository.findFeePaymentsByStudentId(studentId, startDate, endDate);
    }

    public List<Payment> getStudentPayments(String studentId) {
        return paymentRepository.findFeePaymentsByStudentId(studentId);
    }

    public Map<String, Object> getTransactionStats() {
        return paymentRepository.getTransactionStats();
    }

    public List<Payment> getFilteredTransactions(String type, String mode, String startDate, String endDate, String search, int page, int pageSize) {
        return paymentRepository.findTransactions(type, mode, startDate, endDate, search, page, pageSize);
    }

    public List<Payment> getFilteredTransactions(String type, String mode, String search, int page, int pageSize) {
        return paymentRepository.findTransactions(type, mode, search, page, pageSize);
    }

    public int countFilteredTransactions(String type, String mode, String startDate, String endDate, String search) {
        return paymentRepository.countTransactions(type, mode, startDate, endDate, search);
    }

    public int countFilteredTransactions(String type, String mode, String search) {
        return paymentRepository.countTransactions(type, mode, search);
    }

    public List<String> getAvailablePaymentTypes() {
        return paymentRepository.getAvailablePaymentTypes();
    }

    public List<String> getAvailablePaymentModes() {
        return paymentRepository.getAvailablePaymentModes();
    }
}
