package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Payment;
import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import com.example.SchoolWebsite.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
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

    public void saveStaff(Staff s) {
        staffRepository.save(s);
    }

    public void updateStaff(Staff s) {
        staffRepository.update(s);
    }

    public void deleteStaff(String employeeId) {
        staffRepository.delete(employeeId);
    }
}
