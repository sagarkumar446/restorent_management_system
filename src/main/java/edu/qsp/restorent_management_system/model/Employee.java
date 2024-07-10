package edu.qsp.restorent_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Employee {
    @Id
    private int employeeId;
    private String name;
    private String role;
    private long contact;
    private String email;
    private double salary;

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public long getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    
}
