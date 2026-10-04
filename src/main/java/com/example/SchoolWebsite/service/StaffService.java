package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import com.example.SchoolWebsite.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class StaffService {

    private static final List<String> DEFAULT_ROLES = List.of(
        "Driver",
        "Accountant",
        "Peon",
        "Cleaning Staff",
        "Librarian",
        "Security Guard",
        "Lab Assistant",
        "Office Clerk"
    );

    private final StaffRepository staffRepository;
    private final com.example.SchoolWebsite.repository.UserRepository userRepository;

    public StaffService(StaffRepository staffRepository, com.example.SchoolWebsite.repository.UserRepository userRepository) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    public Staff getStaffProfile(String employeeId) {
        return staffRepository.findById(employeeId);
    }

    public List<Payment> getSalaryHistory(String employeeId) {
        return staffRepository.getSalaryHistory(employeeId);
    }

    public VehicleInfo getAssignedVehicle(String driverId) {
        return staffRepository.getAssignedVehicle(driverId);
    }

    public List<Student> getBusStudents(String vehicleId) {
        return staffRepository.getBusStudents(vehicleId);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public List<String> getAllRoles() {
        Set<String> roles = new LinkedHashSet<>(DEFAULT_ROLES);
        List<String> dbRoles = staffRepository.findDistinctRoles();
        if (dbRoles != null) {
            for (String role : dbRoles) {
                if (role != null && !role.trim().isEmpty()) {
                    roles.add(role.trim());
                }
            }
        }
        return new ArrayList<>(roles);
    }

    public void saveStaff(Staff s) {
        staffRepository.save(s);
        userRepository.createOrUpdate(s.getEmployeeId(), "pass_" + s.getEmployeeId(), "staff");
    }

    public void updateStaff(Staff s) {
        staffRepository.update(s);
    }

    public void deleteStaff(String employeeId) {
        userRepository.delete(employeeId);
        staffRepository.delete(employeeId);
    }
}
