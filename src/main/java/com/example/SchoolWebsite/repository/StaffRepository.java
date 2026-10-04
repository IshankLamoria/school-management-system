package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StaffRepository {

    private final JdbcTemplate jdbcTemplate;

    public StaffRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method 1: Get staff profile by Employee ID
    public Staff findById(String employeeId) {
        String sql = "SELECT Employee_ID, First_Name, Middle_Name, Last_Name, " +
                     "Contact_Number, Designated_Role, Date_Of_Joining, Salary " +
                     "FROM Staff WHERE Employee_ID = ?";

        List<Staff> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Staff s = new Staff();
            s.setEmployeeId(rs.getString("Employee_ID"));
            s.setFirstName(rs.getString("First_Name"));
            s.setMiddleName(rs.getString("Middle_Name"));
            s.setLastName(rs.getString("Last_Name"));
            s.setContactNumber(rs.getString("Contact_Number"));
            s.setDesignatedRole(rs.getString("Designated_Role"));
            s.setDateOfJoining(rs.getString("Date_Of_Joining"));
            s.setSalary(rs.getDouble("Salary"));
            return s;
        }, employeeId);

        return list.isEmpty() ? null : list.get(0);
    }

    // Method 2: Get salary payment transactions for staff
    public List<Payment> getSalaryHistory(String employeeId) {
        String sql = "SELECT p.Transaction_ID, p.Date_Of_Payment, p.Payment_Type, " +
                     "p.Receiver, p.Sender, p.Payment_Mode, p.Amount " +
                     "FROM Staff_Salary ss " +
                     "JOIN Payments p ON ss.Payment_ID = p.Transaction_ID " +
                     "WHERE ss.Staff_ID = ? " +
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
        }, employeeId);
    }

    // Method 3: Get assigned vehicle for driver
    public VehicleInfo getAssignedVehicle(String driverId) {
        String sql = "SELECT Vehicle_ID, Route_Name, Registration_Number, Capacity, DriverID " +
                     "FROM Transport WHERE DriverID = ?";

        List<VehicleInfo> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            VehicleInfo v = new VehicleInfo();
            v.setVehicleId(rs.getString("Vehicle_ID"));
            v.setRouteName(rs.getString("Route_Name"));
            v.setRegistrationNumber(rs.getString("Registration_Number"));
            v.setCapacity(rs.getInt("Capacity"));
            v.setDriverId(rs.getString("DriverID"));
            return v;
        }, driverId);

        return list.isEmpty() ? null : list.get(0);
    }

    // Method 4: Get students assigned to a specific vehicle (NO fees, only transit/safety info)
    public List<Student> getBusStudents(String vehicleId) {
        String sql = "SELECT Admission_No AS admissionNo, Roll_No AS rollNo, First_Name AS firstName, " +
                     "Last_Name AS lastName, Standard AS standard, Division AS division, " +
                     "House AS house, Blood_Group AS bloodGroup " +
                     "FROM Student " +
                     "WHERE Vehicle_No = ? " +
                     "ORDER BY Standard, Division, Roll_No";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("admissionNo"));
            s.setRollNo(rs.getInt("rollNo"));
            s.setFirstName(rs.getString("firstName"));
            s.setLastName(rs.getString("lastName"));
            s.setStandard(rs.getString("standard"));
            s.setDivision(rs.getString("division"));
            s.setHouse(rs.getString("house"));
            s.setBloodGroup(rs.getString("bloodGroup"));
            return s;
        }, vehicleId);
    }

    // Method 5: Find all staff members for Admin
    public List<Staff> findAll() {
        String sql = "SELECT Employee_ID, First_Name, Middle_Name, Last_Name, " +
                     "Contact_Number, Designated_Role, Date_Of_Joining, Salary " +
                     "FROM Staff ORDER BY Employee_ID ASC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Staff s = new Staff();
            s.setEmployeeId(rs.getString("Employee_ID"));
            s.setFirstName(rs.getString("First_Name"));
            s.setMiddleName(rs.getString("Middle_Name"));
            s.setLastName(rs.getString("Last_Name"));
            s.setContactNumber(rs.getString("Contact_Number"));
            s.setDesignatedRole(rs.getString("Designated_Role"));
            s.setDateOfJoining(rs.getString("Date_Of_Joining"));
            s.setSalary(rs.getDouble("Salary"));
            return s;
        });
    }

    // Method 6: Save staff
    public void save(Staff s) {
        String sql = "INSERT INTO Staff (Employee_ID, First_Name, Middle_Name, Last_Name, " +
                     "Contact_Number, Designated_Role, Date_Of_Joining, Salary) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            s.getEmployeeId(),
            s.getFirstName(),
            s.getMiddleName() != null && s.getMiddleName().trim().isEmpty() ? null : s.getMiddleName(),
            s.getLastName() != null && s.getLastName().trim().isEmpty() ? null : s.getLastName(),
            s.getContactNumber(),
            s.getDesignatedRole(),
            s.getDateOfJoining(),
            s.getSalary()
        );
    }

    // Method 7: Update staff
    public void update(Staff s) {
        String sql = "UPDATE Staff SET First_Name = ?, Middle_Name = ?, Last_Name = ?, " +
                     "Contact_Number = ?, Designated_Role = ?, Date_Of_Joining = ?, Salary = ? " +
                     "WHERE Employee_ID = ?";

        jdbcTemplate.update(sql,
            s.getFirstName(),
            s.getMiddleName() != null && s.getMiddleName().trim().isEmpty() ? null : s.getMiddleName(),
            s.getLastName() != null && s.getLastName().trim().isEmpty() ? null : s.getLastName(),
            s.getContactNumber(),
            s.getDesignatedRole(),
            s.getDateOfJoining(),
            s.getSalary(),
            s.getEmployeeId()
        );
    }

    // Method 8: Delete staff
    public void delete(String employeeId) {
        jdbcTemplate.update("DELETE FROM Staff WHERE Employee_ID = ?", employeeId);
    }

    // Method 9: Get distinct designated roles
    public List<String> findDistinctRoles() {
        String sql = "SELECT DISTINCT Designated_Role FROM Staff " +
                     "WHERE Designated_Role IS NOT NULL AND TRIM(Designated_Role) <> '' " +
                     "ORDER BY Designated_Role ASC";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
