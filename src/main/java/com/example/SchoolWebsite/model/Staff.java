package com.example.SchoolWebsite.model;

public class Staff {
    private String employeeId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private String designatedRole;
    private String dateOfJoining;
    private double salary;

    public Staff() {}

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) sb.append(firstName);
        if (middleName != null && !middleName.trim().isEmpty()) sb.append(" ").append(middleName);
        if (lastName != null && !lastName.trim().isEmpty()) sb.append(" ").append(lastName);
        return sb.toString();
    }

    public boolean isDriver() {
        return designatedRole != null && "Driver".equalsIgnoreCase(designatedRole.trim());
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getDesignatedRole() { return designatedRole; }
    public void setDesignatedRole(String designatedRole) { this.designatedRole = designatedRole; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
