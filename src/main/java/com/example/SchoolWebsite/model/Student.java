package com.example.SchoolWebsite.model;

public class Student {
    private String admissionNo;
    private int rollNo;
    private String firstName;
    private String standard;
    private String house;
    private double remainingFees;

    // Constructors
    public Student() {}

    public Student(String admissionNo, int rollNo, String firstName, String standard, String house, double remainingFees) {
        this.admissionNo = admissionNo;
        this.rollNo = rollNo;
        this.firstName = firstName;
        this.standard = standard;
        this.house = house;
        this.remainingFees = remainingFees;
    }

    // Getters and Setters
    public String getAdmissionNo() { return admissionNo; }
    public void setAdmissionNo(String admissionNo) { this.admissionNo = admissionNo; }

    public int getRollNo() { return rollNo; }
    public void setRollNo(int rollNo) { this.rollNo = rollNo; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }

    public double getRemainingFees() { return remainingFees; }
    public void setRemainingFees(double remainingFees) { this.remainingFees = remainingFees; }

    @Override
    public String toString() {
        return "Student{admissionNo='" + admissionNo + "', firstName='" + firstName + "', standard='" + standard + "'}";
    }
}
