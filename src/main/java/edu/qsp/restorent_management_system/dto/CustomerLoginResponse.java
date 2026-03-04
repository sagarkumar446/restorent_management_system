package edu.qsp.restorent_management_system.dto;

public class CustomerLoginResponse {
    private Long customerId;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private Double loyaltyPoints;
    private Boolean isActive;

    public CustomerLoginResponse() {
    }

    public CustomerLoginResponse(Long customerId, String name, String email, String contactNumber, String address, Double loyaltyPoints, Boolean isActive) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.loyaltyPoints = loyaltyPoints;
        this.isActive = isActive;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Double loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
