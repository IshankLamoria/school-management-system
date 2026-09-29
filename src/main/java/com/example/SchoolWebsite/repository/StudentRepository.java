package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository tells Spring: "create and manage one instance of this class".
@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection for JdbcTemplate
    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method 1: Get all students
    public List<Student> findAll() {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Standard AS standard, House AS house, Remaining_Fees AS remainingFees FROM Student";

        // query() runs the SQL and this returns the student object list (list of student object)
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setStandard(rs.getString("standard"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        });
    }

    // Method 2: Get a single student by their Admission Number (for profile view)
    public Student findByAdmissionNo(String admissionNo) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Standard AS standard, House AS house, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Admission_No = ?";

        // queryForObject is used when expecting a single matching record
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setStandard(rs.getString("standard"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, admissionNo);
    }

    // Method 3: Get all students filtered by a specific Standard/Class (e.g., "10")
    public List<Student> findByStandard(String standard) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Standard AS standard, House AS house, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Standard = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setStandard(rs.getString("standard"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, standard);
    }
}
