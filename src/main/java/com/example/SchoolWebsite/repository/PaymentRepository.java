package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Payment> findFeePaymentsByStudentId(String studentId, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder(
            "SELECT p.Transaction_ID, p.Date_Of_Payment, p.Payment_Type, " +
            "p.Receiver, p.Sender, p.Payment_Mode, p.Amount " +
            "FROM Fees f " +
            "JOIN Payments p ON f.Payment_ID = p.Transaction_ID " +
            "WHERE f.Student_ID = ? "
        );

        List<Object> params = new ArrayList<>();
        params.add(studentId);

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment >= ? ");
            params.add(startDate.trim() + " 00:00:00");
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment <= ? ");
            params.add(endDate.trim() + " 23:59:59");
        }

        sql.append("ORDER BY p.Date_Of_Payment DESC");

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setTransactionId(rs.getString("Transaction_ID"));
            p.setDateOfPayment(rs.getString("Date_Of_Payment"));
            p.setPaymentType(rs.getString("Payment_Type"));
            p.setReceiver(rs.getString("Receiver"));
            p.setSender(rs.getString("Sender"));
            p.setPaymentMode(rs.getString("Payment_Mode"));
            p.setAmount(rs.getDouble("Amount"));
            return p;
        }, params.toArray());
    }

    public List<Payment> findFeePaymentsByStudentId(String studentId) {
        return findFeePaymentsByStudentId(studentId, null, null);
    }

    public Map<String, Object> getTransactionStats() {
        String sql = "SELECT " +
                     "COALESCE(SUM(CASE WHEN Payment_Type = 'Fee' THEN Amount ELSE 0 END), 0) AS totalFee, " +
                     "COALESCE(SUM(CASE WHEN Payment_Type = 'Salary' THEN Amount ELSE 0 END), 0) AS totalSalary, " +
                     "COUNT(*) AS totalCount, " +
                     "COUNT(CASE WHEN Payment_Type = 'Fee' THEN 1 END) AS feeCount, " +
                     "COUNT(CASE WHEN Payment_Type = 'Salary' THEN 1 END) AS salaryCount " +
                     "FROM Payments";

        return jdbcTemplate.queryForMap(sql);
    }

    public List<Payment> findTransactions(String type, String mode, String startDate, String endDate, String search, int page, int pageSize) {
        StringBuilder sql = new StringBuilder(
            "SELECT p.Transaction_ID, p.Date_Of_Payment, p.Payment_Type, p.Receiver, p.Sender, " +
            "p.Payment_Mode, p.Amount, " +
            "COALESCE(f.Student_ID, ss.Staff_ID, ts.Teacher_ID) AS Related_ID, " +
            "CASE " +
            "    WHEN f.Student_ID IS NOT NULL THEN 'Student' " +
            "    WHEN ts.Teacher_ID IS NOT NULL THEN 'Teacher' " +
            "    WHEN ss.Staff_ID IS NOT NULL THEN 'Staff' " +
            "    ELSE 'Other' " +
            "END AS Related_Role " +
            "FROM Payments p " +
            "LEFT JOIN Fees f ON p.Transaction_ID = f.Payment_ID " +
            "LEFT JOIN Staff_Salary ss ON p.Transaction_ID = ss.Payment_ID " +
            "LEFT JOIN Teacher_Salary ts ON p.Transaction_ID = ts.Payment_ID " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (type != null && !type.trim().isEmpty()) {
            sql.append("AND p.Payment_Type = ? ");
            params.add(type.trim());
        }

        if (mode != null && !mode.trim().isEmpty()) {
            sql.append("AND p.Payment_Mode = ? ");
            params.add(mode.trim());
        }

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment >= ? ");
            params.add(startDate.trim() + " 00:00:00");
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment <= ? ");
            params.add(endDate.trim() + " 23:59:59");
        }

        if (search != null && !search.trim().isEmpty()) {
            String pattern = "%" + search.trim() + "%";
            sql.append("AND (p.Transaction_ID LIKE ? OR p.Sender LIKE ? OR p.Receiver LIKE ? OR f.Student_ID LIKE ? OR ss.Staff_ID LIKE ? OR ts.Teacher_ID LIKE ?) ");
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        sql.append("ORDER BY p.Date_Of_Payment DESC, p.Transaction_ID DESC ");

        int offset = Math.max(0, (page - 1) * pageSize);
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setTransactionId(rs.getString("Transaction_ID"));
            p.setDateOfPayment(rs.getString("Date_Of_Payment"));
            p.setPaymentType(rs.getString("Payment_Type"));
            p.setReceiver(rs.getString("Receiver"));
            p.setSender(rs.getString("Sender"));
            p.setPaymentMode(rs.getString("Payment_Mode"));
            p.setAmount(rs.getDouble("Amount"));
            p.setRelatedId(rs.getString("Related_ID"));
            p.setRelatedRole(rs.getString("Related_Role"));
            return p;
        }, params.toArray());
    }

    public List<Payment> findTransactions(String type, String mode, String search, int page, int pageSize) {
        return findTransactions(type, mode, null, null, search, page, pageSize);
    }

    public int countTransactions(String type, String mode, String startDate, String endDate, String search) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) " +
            "FROM Payments p " +
            "LEFT JOIN Fees f ON p.Transaction_ID = f.Payment_ID " +
            "LEFT JOIN Staff_Salary ss ON p.Transaction_ID = ss.Payment_ID " +
            "LEFT JOIN Teacher_Salary ts ON p.Transaction_ID = ts.Payment_ID " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (type != null && !type.trim().isEmpty()) {
            sql.append("AND p.Payment_Type = ? ");
            params.add(type.trim());
        }

        if (mode != null && !mode.trim().isEmpty()) {
            sql.append("AND p.Payment_Mode = ? ");
            params.add(mode.trim());
        }

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment >= ? ");
            params.add(startDate.trim() + " 00:00:00");
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND p.Date_Of_Payment <= ? ");
            params.add(endDate.trim() + " 23:59:59");
        }

        if (search != null && !search.trim().isEmpty()) {
            String pattern = "%" + search.trim() + "%";
            sql.append("AND (p.Transaction_ID LIKE ? OR p.Sender LIKE ? OR p.Receiver LIKE ? OR f.Student_ID LIKE ? OR ss.Staff_ID LIKE ? OR ts.Teacher_ID LIKE ?) ");
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
        return count != null ? count : 0;
    }

    public int countTransactions(String type, String mode, String search) {
        return countTransactions(type, mode, null, null, search);
    }

    public List<String> getAvailablePaymentTypes() {
        String sql = "SELECT DISTINCT Payment_Type FROM Payments ORDER BY Payment_Type";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getAvailablePaymentModes() {
        String sql = "SELECT DISTINCT Payment_Mode FROM Payments ORDER BY Payment_Mode";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
