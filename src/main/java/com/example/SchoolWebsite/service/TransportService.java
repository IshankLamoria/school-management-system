package com.example.SchoolWebsite.service;

import com.example.SchoolWebsite.model.Staff;
import com.example.SchoolWebsite.model.Student;
import com.example.SchoolWebsite.model.VehicleInfo;
import com.example.SchoolWebsite.repository.TransportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TransportService {

    private final TransportRepository transportRepository;

    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<VehicleInfo> getAllVehicles() {
        return transportRepository.getAllVehiclesWithDetails();
    }

    public VehicleInfo getVehicleById(String vehicleId) {
        return transportRepository.getVehicleById(vehicleId);
    }

    public List<Student> getStudentsByVehicleId(String vehicleId) {
        return transportRepository.getStudentsByVehicleId(vehicleId);
    }

    public List<Staff> getAllDrivers() {
        return transportRepository.getAllDrivers();
    }

    public Map<String, Object> getTransportStats() {
        return transportRepository.getTransportStats();
    }

    public void saveVehicle(VehicleInfo vehicle) {
        transportRepository.saveVehicle(vehicle);
    }

    public void updateVehicle(VehicleInfo vehicle) {
        transportRepository.updateVehicle(vehicle);
    }

    public void deleteVehicle(String vehicleId) {
        transportRepository.deleteVehicle(vehicleId);
    }

    public void removeStudentFromVehicle(String admissionNo) {
        transportRepository.removeStudentFromVehicle(admissionNo);
    }

    public void assignStudentToVehicle(String admissionNo, String vehicleId) {
        transportRepository.assignStudentToVehicle(admissionNo, vehicleId);
    }

    public List<Student> getUnassignedStudents() {
        return transportRepository.getUnassignedStudents();
    }
}
