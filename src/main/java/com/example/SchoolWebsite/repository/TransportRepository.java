package com.example.SchoolWebsite.repository;

import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransportRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VehicleInfo> getAllVehiclesWithDetails() {
        String sql = "SELECT t.Vehicle_ID, t.Route_Name, t.Registration_Number, t.Capacity, t.DriverID, " +
                     "CONCAT(s.First_Name, ' ', COALESCE(s.Last_Name, '')) AS Driver_Name, " +
                     "s.Contact_Number AS Driver_Contact, " +
                     "(SELECT COUNT(*) FROM Student st WHERE st.Vehicle_No = t.Vehicle_ID) AS Assigned_Students " +
                     "FROM Transport t " +
                     "LEFT JOIN Staff s ON t.DriverID = s.Employee_ID " +
                     "ORDER BY t.Vehicle_ID";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            VehicleInfo v = new VehicleInfo();
            v.setVehicleId(rs.getString("Vehicle_ID"));
            v.setRouteName(rs.getString("Route_Name"));
            v.setRegistrationNumber(rs.getString("Registration_Number"));
            v.setCapacity(rs.getInt("Capacity"));
            v.setDriverId(rs.getString("DriverID"));
            v.setDriverName(rs.getString("Driver_Name") != null ? rs.getString("Driver_Name").trim() : null);
            v.setDriverContact(rs.getString("Driver_Contact"));
            v.setAssignedStudents(rs.getInt("Assigned_Students"));
            return v;
        });
    }

    public VehicleInfo getVehicleById(String vehicleId) {
        String sql = "SELECT t.Vehicle_ID, t.Route_Name, t.Registration_Number, t.Capacity, t.DriverID, " +
                     "CONCAT(s.First_Name, ' ', COALESCE(s.Last_Name, '')) AS Driver_Name, " +
                     "s.Contact_Number AS Driver_Contact, " +
                     "(SELECT COUNT(*) FROM Student st WHERE st.Vehicle_No = t.Vehicle_ID) AS Assigned_Students " +
                     "FROM Transport t " +
                     "LEFT JOIN Staff s ON t.DriverID = s.Employee_ID " +
                     "WHERE t.Vehicle_ID = ?";

        List<VehicleInfo> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            VehicleInfo v = new VehicleInfo();
            v.setVehicleId(rs.getString("Vehicle_ID"));
            v.setRouteName(rs.getString("Route_Name"));
            v.setRegistrationNumber(rs.getString("Registration_Number"));
            v.setCapacity(rs.getInt("Capacity"));
            v.setDriverId(rs.getString("DriverID"));
            v.setDriverName(rs.getString("Driver_Name") != null ? rs.getString("Driver_Name").trim() : null);
            v.setDriverContact(rs.getString("Driver_Contact"));
            v.setAssignedStudents(rs.getInt("Assigned_Students"));
            return v;
        }, vehicleId);

        return list.isEmpty() ? null : list.get(0);
    }

    public List<Student> getStudentsByVehicleId(String vehicleId) {
        String sql = "SELECT Admission_No, Roll_No, First_Name, Middle_Name, Last_Name, " +
                     "Standard, Division, House, Blood_Group, Remaining_Fees " +
                     "FROM Student " +
                     "WHERE Vehicle_No = ? " +
                     "ORDER BY Standard, Division, Roll_No";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("Admission_No"));
            s.setRollNo(rs.getInt("Roll_No"));
            s.setFirstName(rs.getString("First_Name"));
            s.setMiddleName(rs.getString("Middle_Name"));
            s.setLastName(rs.getString("Last_Name"));
            s.setStandard(rs.getString("Standard"));
            s.setDivision(rs.getString("Division"));
            s.setHouse(rs.getString("House"));
            s.setBloodGroup(rs.getString("Blood_Group"));
            s.setRemainingFees(rs.getDouble("Remaining_Fees"));
            return s;
        }, vehicleId);
    }

    public List<Staff> getAllDrivers() {
        String sql = "SELECT Employee_ID, First_Name, Middle_Name, Last_Name, " +
                     "Contact_Number, Designated_Role, Date_Of_Joining, Salary " +
                     "FROM Staff " +
                     "WHERE Designated_Role = 'Driver' " +
                     "ORDER BY Employee_ID";

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

    public Map<String, Object> getTransportStats() {
        String sql = "SELECT " +
                     "COUNT(*) AS totalVehicles, " +
                     "COALESCE(SUM(Capacity), 0) AS totalCapacity " +
                     "FROM Transport";

        Map<String, Object> stats = jdbcTemplate.queryForMap(sql);
        Map<String, Object> result = new HashMap<>(stats);

        Integer assignedCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Student WHERE Vehicle_No IS NOT NULL", Integer.class);
        Integer unassignedCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM Student WHERE Vehicle_No IS NULL", Integer.class);

        int assigned = assignedCount != null ? assignedCount : 0;
        int unassigned = unassignedCount != null ? unassignedCount : 0;
        Number capNum = (Number) result.get("totalCapacity");
        int capacity = capNum != null ? capNum.intValue() : 0;

        result.put("assignedStudents", assigned);
        result.put("unassignedStudents", unassigned);
        int utilization = capacity > 0 ? (int) Math.round(((double) assigned / capacity) * 100) : 0;
        result.put("utilizationPercentage", utilization);

        return result;
    }

    public void saveVehicle(VehicleInfo vehicle) {
        String sql = "INSERT INTO Transport (Vehicle_ID, Route_Name, Registration_Number, Capacity, DriverID) " +
                     "VALUES (?, ?, ?, ?, ?)";
        String driverId = (vehicle.getDriverId() != null && !vehicle.getDriverId().trim().isEmpty()) 
                          ? vehicle.getDriverId().trim() : null;
        jdbcTemplate.update(sql, vehicle.getVehicleId(), vehicle.getRouteName(), 
                            vehicle.getRegistrationNumber(), vehicle.getCapacity(), driverId);
    }

    public void updateVehicle(VehicleInfo vehicle) {
        String sql = "UPDATE Transport SET Route_Name = ?, Registration_Number = ?, Capacity = ?, DriverID = ? " +
                     "WHERE Vehicle_ID = ?";
        String driverId = (vehicle.getDriverId() != null && !vehicle.getDriverId().trim().isEmpty()) 
                          ? vehicle.getDriverId().trim() : null;
        jdbcTemplate.update(sql, vehicle.getRouteName(), vehicle.getRegistrationNumber(), 
                            vehicle.getCapacity(), driverId, vehicle.getVehicleId());
    }

    public void deleteVehicle(String vehicleId) {
        String sql = "DELETE FROM Transport WHERE Vehicle_ID = ?";
        jdbcTemplate.update(sql, vehicleId);
    }

    public void removeStudentFromVehicle(String admissionNo) {
        String sql = "UPDATE Student SET Vehicle_No = NULL WHERE Admission_No = ?";
        jdbcTemplate.update(sql, admissionNo);
    }

    public void assignStudentToVehicle(String admissionNo, String vehicleId) {
        String sql = "UPDATE Student SET Vehicle_No = ? WHERE Admission_No = ?";
        jdbcTemplate.update(sql, vehicleId, admissionNo);
    }

    public List<Student> getUnassignedStudents() {
        String sql = "SELECT Admission_No, Roll_No, First_Name, Middle_Name, Last_Name, " +
                     "Standard, Division, House, Blood_Group, Remaining_Fees " +
                     "FROM Student " +
                     "WHERE Vehicle_No IS NULL " +
                     "ORDER BY Standard, Division, Roll_No";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Student s = new Student();
            s.setAdmissionNo(rs.getString("Admission_No"));
            s.setRollNo(rs.getInt("Roll_No"));
            s.setFirstName(rs.getString("First_Name"));
            s.setMiddleName(rs.getString("Middle_Name"));
            s.setLastName(rs.getString("Last_Name"));
            s.setStandard(rs.getString("Standard"));
            s.setDivision(rs.getString("Division"));
            s.setHouse(rs.getString("House"));
            s.setBloodGroup(rs.getString("Blood_Group"));
            s.setRemainingFees(rs.getDouble("Remaining_Fees"));
            return s;
        });
    }
}
