package com.example.SchoolWebsite.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Repository for aggregate statistics displayed on the home page
@Repository
public class StatsRepository {

    private final JdbcTemplate jdbcTemplate;

    public StatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long countStudents() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Student", Long.class);
        return count != null ? count : 0;
    }

    public long countTeachers() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Teacher", Long.class);
        return count != null ? count : 0;
    }

    public long countStaff() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Staff", Long.class);
        return count != null ? count : 0;
    }

    public long countHouses() {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM House", Long.class);
        return count != null ? count : 0;
    }

    // Used by LoginController to validate login credentials
    public boolean studentExists(String admissionNo) {
        Long count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Student WHERE Admission_No = ?", Long.class, admissionNo);
        return count != null && count > 0;
    }

    public boolean teacherExists(String employeeId) {
        Long count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Teacher WHERE Employee_ID = ?", Long.class, employeeId);
        return count != null && count > 0;
    }

    public boolean staffExists(String employeeId) {
        Long count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Staff WHERE Employee_ID = ?", Long.class, employeeId);
        return count != null && count > 0;
    }
}
