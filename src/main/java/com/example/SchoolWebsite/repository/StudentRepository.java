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

    // Get all students (for student directory listing)
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

    // Get a single student by their Admission Number (full record)
    public Student findByAdmissionNo(String admissionNo) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Middle_Name AS middleName, Last_Name AS lastName, Blood_Group AS bloodGroup, " +
                     "Standard AS standard, Division AS division, House AS house, Vehicle_No AS vehicleNo, " +
                     "Date_Of_Admission AS dateOfAdmission, Date_Of_Birth AS dateOfBirth, Age AS age, " +
                     "Gender AS gender, Remaining_Fees AS remainingFees " +
                     "FROM Student WHERE Admission_No = ?";

        List<Student> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setMiddleName(rs.getString("middleName"));
            s.setLastName(rs.getString("lastName"));
            s.setBloodGroup(rs.getString("bloodGroup"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setVehicleNo(rs.getString("vehicleNo"));
            s.setDateOfAdmission(rs.getString("dateOfAdmission"));
            s.setDateOfBirth(rs.getString("dateOfBirth"));
            s.setAge(rs.getInt("age"));
            s.setGender(rs.getString("gender"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, admissionNo);

        return list.isEmpty() ? null : list.get(0);
    }

    // Get all students filtered by a specific Standard/Class (e.g., "10")
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

    // Get students of a specific class and section (Standard and Division)
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

    // Get FULL student profile with JOINs (for the profile page)
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

    // Search and filter students for Admin
    public List<Student> searchAndFilter(String standard, String division, String search) {
        StringBuilder sql = new StringBuilder(
            "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
            "Middle_Name AS middleName, Last_Name AS lastName, Blood_Group AS bloodGroup, " +
            "Standard AS standard, Division AS division, House AS house, Vehicle_No AS vehicleNo, " +
            "Date_Of_Admission AS dateOfAdmission, Date_Of_Birth AS dateOfBirth, Age AS age, " +
            "Gender AS gender, Remaining_Fees AS remainingFees FROM Student WHERE 1=1"
        );

        java.util.List<Object> params = new java.util.ArrayList<>();

        if (standard != null && !standard.trim().isEmpty()) {
            sql.append(" AND Standard = ?");
            params.add(standard.trim());
        }
        if (division != null && !division.trim().isEmpty()) {
            sql.append(" AND Division = ?");
            params.add(division.trim());
        }
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (LOWER(First_Name) LIKE ? OR LOWER(Last_Name) LIKE ? OR LOWER(Admission_No) LIKE ?)");
            String term = "%" + search.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        sql.append(" ORDER BY Standard, Division, Roll_No ASC");

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setMiddleName(rs.getString("middleName"));
            s.setLastName(rs.getString("lastName"));
            s.setBloodGroup(rs.getString("bloodGroup"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setVehicleNo(rs.getString("vehicleNo"));
            s.setDateOfAdmission(rs.getString("dateOfAdmission"));
            s.setDateOfBirth(rs.getString("dateOfBirth"));
            s.setAge(rs.getInt("age"));
            s.setGender(rs.getString("gender"));
            s.setRemainingFees(rs.getDouble("remainingFees"));
            return s;
        }, params.toArray());
    }

    // Insert new Student
    public void save(Student s) {
        String sql = "INSERT INTO Student (Admission_No, Roll_No, First_Name, Middle_Name, Last_Name, " +
                     "Blood_Group, Standard, Division, House, Vehicle_No, Date_Of_Admission, " +
                     "Date_Of_Birth, Age, Gender, Remaining_Fees) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            s.getAdmissionNo(),
            s.getRollNo(),
            s.getFirstName(),
            s.getMiddleName() != null && s.getMiddleName().trim().isEmpty() ? null : s.getMiddleName(),
            s.getLastName() != null && s.getLastName().trim().isEmpty() ? null : s.getLastName(),
            s.getBloodGroup(),
            s.getStandard(),
            s.getDivision(),
            s.getHouse(),
            s.getVehicleNo() != null && s.getVehicleNo().trim().isEmpty() ? null : s.getVehicleNo(),
            s.getDateOfAdmission(),
            s.getDateOfBirth(),
            s.getAge(),
            s.getGender(),
            s.getRemainingFees()
        );
    }

    // Update existing Student
    public void update(Student s) {
        String sql = "UPDATE Student SET Roll_No = ?, First_Name = ?, Middle_Name = ?, Last_Name = ?, " +
                     "Blood_Group = ?, Standard = ?, Division = ?, House = ?, Vehicle_No = ?, " +
                     "Date_Of_Admission = ?, Date_Of_Birth = ?, Age = ?, Gender = ?, Remaining_Fees = ? " +
                     "WHERE Admission_No = ?";

        jdbcTemplate.update(sql,
            s.getRollNo(),
            s.getFirstName(),
            s.getMiddleName() != null && s.getMiddleName().trim().isEmpty() ? null : s.getMiddleName(),
            s.getLastName() != null && s.getLastName().trim().isEmpty() ? null : s.getLastName(),
            s.getBloodGroup(),
            s.getStandard(),
            s.getDivision(),
            s.getHouse(),
            s.getVehicleNo() != null && s.getVehicleNo().trim().isEmpty() ? null : s.getVehicleNo(),
            s.getDateOfAdmission(),
            s.getDateOfBirth(),
            s.getAge(),
            s.getGender(),
            s.getRemainingFees(),
            s.getAdmissionNo()
        );
    }

    // Delete Student
    public void delete(String admissionNo) {
        String sql = "DELETE FROM Student WHERE Admission_No = ?";
        jdbcTemplate.update(sql, admissionNo);
    }

    // Available Classes (Standards)
    public List<String> getAvailableClasses() {
        String sql = "SELECT Standard FROM Class ORDER BY CAST(Standard AS UNSIGNED)";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Available Divisions
    public List<String> getAvailableDivisions() {
        String sql = "SELECT DISTINCT Division FROM Section ORDER BY Division";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Available Sections (Standard and Division)
    public List<java.util.Map<String, Object>> getAvailableSections() {
        String sql = "SELECT Standard, Division, Room_No FROM Section ORDER BY Standard, Division";
        return jdbcTemplate.queryForList(sql);
    }

    // Available Houses
    public List<java.util.Map<String, Object>> getAvailableHouses() {
        String sql = "SELECT Color, House_Name FROM House ORDER BY House_Name";
        return jdbcTemplate.queryForList(sql);
    }

    // Available Vehicles
    public List<java.util.Map<String, Object>> getAvailableVehicles() {
        String sql = "SELECT Vehicle_ID, Route_Name, Registration_Number FROM Transport ORDER BY Vehicle_ID";
        return jdbcTemplate.queryForList(sql);
    }

    //  Save Guardian
    public void saveGuardian(String studentId, String relationship, String firstName, String middleName, String lastName, String occupation, String phone) {
        String sql = "INSERT INTO Student_Guardian (Student_ID, Relationship, First_Name, Middle_Name, Last_Name, Occupation, Phone_No) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE First_Name = VALUES(First_Name), " +
                     "Middle_Name = VALUES(Middle_Name), Last_Name = VALUES(Last_Name), Occupation = VALUES(Occupation), Phone_No = VALUES(Phone_No)";
        jdbcTemplate.update(sql, studentId, relationship, firstName,
            middleName != null && middleName.trim().isEmpty() ? null : middleName,
            lastName != null && lastName.trim().isEmpty() ? null : lastName,
            occupation, phone);
    }
}
