package edu.qsp.restorent_management_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Singleton-style entity to store Razorpay gateway configuration.
 * We always use id = 1 (single row).
 */
@Entity
public class PaymentConfig {

    @Id
    private Integer id = 1; // Always one row

    @Column(nullable = false)
    private String razorpayKeyId = "";

    @Column(nullable = false)
    private String razorpayKeySecret = "";

    private boolean enabled = false;

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazorpayKeyId() {
        return razorpayKeyId;
    }

    public void setRazorpayKeyId(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }

    public String getRazorpayKeySecret() {
        return razorpayKeySecret;
    }

    public void setRazorpayKeySecret(String razorpayKeySecret) {
        this.razorpayKeySecret = razorpayKeySecret;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
