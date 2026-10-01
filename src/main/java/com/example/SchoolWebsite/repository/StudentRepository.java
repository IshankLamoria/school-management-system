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

    // Method 1: Get all students (for student directory listing)
    public List<Student> findAll() {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Last_Name AS lastName, Standard AS standard, Division AS division, " +
                     "House AS house, Remaining_Fees AS remainingFees FROM Student ORDER BY Standard, Division, Roll_No";

        // query() runs the SQL and this returns the student object list (list of student object)
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setLastName(rs.getString("lastName"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        });
    }

    // Method 2: Get a single student by their Admission Number
    public Student findByAdmissionNo(String admissionNo) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Last_Name AS lastName, Standard AS standard, Division AS division, " +
                     "House AS house, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Admission_No = ?";

        // queryForObject is used when expecting a single matching record
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setLastName(rs.getString("lastName"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, admissionNo);
    }

    // Method 3: Get all students filtered by a specific Standard/Class (e.g., "10")
    public List<Student> findByStandard(String standard) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Last_Name AS lastName, Standard AS standard, Division AS division, " +
                     "House AS house, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Standard = ? ORDER BY Division, Roll_No";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setLastName(rs.getString("lastName"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, standard);
    }

    // Method 4: Get students of a specific class and section (Standard and Division)
    public List<Student> findByStandardAndDivision(String standard, String division) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Last_Name AS lastName, Standard AS standard, Division AS division, " +
                     "House AS house, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Standard = ? AND Division = ? ORDER BY Roll_No ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setLastName(rs.getString("lastName"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, standard, division);
    }

    // Method 4: Get FULL student profile with JOINs (for the profile page)
    // Joins Student + House + Section + Teacher (class teacher) + Transport
    public Student findFullProfile(String admissionNo) {
        String sql = "SELECT s.Admission_No, s.Roll_No, s.First_Name, s.Middle_Name, s.Last_Name, " +
                     "s.Blood_Group, s.Standard, s.Division, s.House, s.Vehicle_No, " +
                     "s.Date_Of_Admission, s.Date_Of_Birth, s.Age, s.Gender, s.Remaining_Fees, " +
                     "h.House_Name, " +
                     "sec.Room_No, " +
                     "CONCAT(t.First_Name, ' ', COALESCE(t.Last_Name, '')) AS classTeacherName, " +
                     "tr.Route_Name " +
                     "FROM Student s " +
                     "JOIN House h ON s.House = h.Color " +
                     "JOIN Section sec ON s.Standard = sec.Standard AND s.Division = sec.Division " +
                     "LEFT JOIN Teacher t ON sec.Class_Teacher = t.Employee_ID " +
                     "LEFT JOIN Transport tr ON s.Vehicle_No = tr.Vehicle_ID " +
                     "WHERE s.Admission_No = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("Admission_No"));
            s.setRollNo(rs.getInt("Roll_No"));
            s.setFirstName(rs.getString("First_Name"));
            s.setMiddleName(rs.getString("Middle_Name"));
            s.setLastName(rs.getString("Last_Name"));
            s.setBloodGroup(rs.getString("Blood_Group"));
            s.setStandard(rs.getString("Standard"));
            s.setDivision(rs.getString("Division"));
            s.setHouse(rs.getString("House"));
            s.setVehicleNo(rs.getString("Vehicle_No"));
            s.setDateOfAdmission(rs.getString("Date_Of_Admission"));
            s.setDateOfBirth(rs.getString("Date_Of_Birth"));
            s.setAge(rs.getInt("Age"));
            s.setGender(rs.getString("Gender"));
            s.setRemainingFees(rs.getDouble("Remaining_Fees"));
            s.setHouseName(rs.getString("House_Name"));
            s.setRoomNo(rs.getString("Room_No"));
            s.setClassTeacherName(rs.getString("classTeacherName"));
            s.setRouteName(rs.getString("Route_Name"));
            return s;
        }, admissionNo);
    }
}
