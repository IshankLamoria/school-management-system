package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Payment> findFeePaymentsByStudentId(String studentId) {
        String sql = "SELECT p.Transaction_ID, p.Date_Of_Payment, p.Payment_Type, " +
                     "p.Receiver, p.Sender, p.Payment_Mode, p.Amount " +
                     "FROM Fees f " +
                     "JOIN Payments p ON f.Payment_ID = p.Transaction_ID " +
                     "WHERE f.Student_ID = ? " +
                     "ORDER BY p.Date_Of_Payment DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setTransactionId(rs.getString("Transaction_ID"));
            p.setDateOfPayment(rs.getString("Date_Of_Payment"));
            p.setPaymentType(rs.getString("Payment_Type"));
            p.setReceiver(rs.getString("Receiver"));
            p.setSender(rs.getString("Sender"));
            p.setPaymentMode(rs.getString("Payment_Mode"));
            p.setAmount(rs.getDouble("Amount"));
            return p;
        }, studentId);
    }
}
