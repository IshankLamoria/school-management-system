package com.example.SchoolWebsite.model;

public class VehicleInfo {
    private String vehicleId;
    private String routeName;
    private String registrationNumber;
    private int capacity;
    private String driverId;

    public VehicleInfo() {}

    public VehicleInfo(String vehicleId, String routeName, String registrationNumber, int capacity, String driverId) {
        this.vehicleId = vehicleId;
        this.routeName = routeName;
        this.registrationNumber = registrationNumber;
        this.capacity = capacity;
        this.driverId = driverId;
    }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
}
