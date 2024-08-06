package edu.qsp.restorent_management_system.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Employee implements Serializable{
    @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employe_seq")
    @SequenceGenerator(name = "employe_seq", sequenceName = "employe_seq", allocationSize = 1, initialValue=100)
    private int employeeId;
    private String name;
    private String role;
    private long contact;
    private String email;
    private double salary;
    @OneToMany
    private List<Order> orders;
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

 

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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    
}
