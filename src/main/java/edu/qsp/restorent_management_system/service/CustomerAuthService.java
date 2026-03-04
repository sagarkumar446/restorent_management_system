package edu.qsp.restorent_management_system.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.dto.CustomerLoginRequest;
import edu.qsp.restorent_management_system.dto.CustomerLoginResponse;
import edu.qsp.restorent_management_system.dto.CustomerRegisterRequest;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.repository.CustomerRepository;

@Service
public class CustomerAuthService {

    @Autowired
    private CustomerRepository customerRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Register a new customer
     */
    public Customer registerCustomer(CustomerRegisterRequest request) throws Exception {
        // Validate input
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new Exception("Name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new Exception("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new Exception("Password is required");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new Exception("Passwords do not match");
        }
        if (request.getPassword().length() < 6) {
            throw new Exception("Password must be at least 6 characters long");
        }

        // Check if email already exists
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());
        if (existingCustomer.isPresent()) {
            throw new Exception("Email already registered");
        }

        // Check if contact number already exists
        if (request.getContactNumber() != null && !request.getContactNumber().isEmpty()) {
            Optional<Customer> existingByPhone = customerRepository.findByContactNumber(request.getContactNumber());
            if (existingByPhone.isPresent()) {
                throw new Exception("Contact number already registered");
            }
        }

        // Create new customer
        Customer newCustomer = new Customer();
        newCustomer.setName(request.getName());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setContactNumber(request.getContactNumber());
        newCustomer.setAddress(request.getAddress());
        newCustomer.setPassword(encodePassword(request.getPassword()));
        newCustomer.setRegistrationDate(LocalDateTime.now().format(formatter));
        newCustomer.setIsActive(true);
        newCustomer.setLoyaltyPoints(0.0);

        return customerRepository.save(newCustomer);
    }

    /**
     * Login customer with email and password
     */
    public CustomAuthSession loginCustomer(CustomerLoginRequest request) throws Exception {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new Exception("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new Exception("Password is required");
        }

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(request.getEmail());
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Invalid email or password");
        }

        Customer customer = optionalCustomer.get();

        if (!customer.getIsActive()) {
            throw new Exception("Customer account is inactive");
        }

        if (!verifyPassword(request.getPassword(), customer.getPassword())) {
            throw new Exception("Invalid email or password");
        }

        // Update last login date
        customer.setLastLoginDate(LocalDateTime.now().format(formatter));
        customerRepository.save(customer);

        // Create session
        CustomAuthSession session = new CustomAuthSession();
        session.setCustomerId(customer.getCustomerId());
        session.setEmail(customer.getEmail());
        session.setName(customer.getName());
        session.setLoginTime(LocalDateTime.now().format(formatter));

        return session;
    }

    /**
     * Logout customer
     */
    public boolean logoutCustomer(Long customerId) throws Exception {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if (!customer.isPresent()) {
            throw new Exception("Customer not found");
        }
        // In a real application with JWT tokens, you would invalidate the token here
        return true;
    }

    /**
     * Get customer profile
     */
    public CustomerLoginResponse getCustomerProfile(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        return new CustomerLoginResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getContactNumber(),
                customer.getAddress(),
                customer.getLoyaltyPoints(),
                customer.getIsActive()
        );
    }

    /**
     * Update customer profile
     */
    public Customer updateCustomerProfile(Long customerId, CustomerRegisterRequest request) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            customer.setName(request.getName());
        }

        if (request.getContactNumber() != null && !request.getContactNumber().isEmpty()) {
            // Check if new contact number is already in use by another customer
            Optional<Customer> existingByPhone = customerRepository.findByContactNumber(request.getContactNumber());
            if (existingByPhone.isPresent() && !existingByPhone.get().getCustomerId().equals(customerId)) {
                throw new Exception("Contact number already in use");
            }
            customer.setContactNumber(request.getContactNumber());
        }

        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            customer.setAddress(request.getAddress());
        }

        return customerRepository.save(customer);
    }

    /**
     * Change password
     */
    public boolean changePassword(Long customerId, String oldPassword, String newPassword) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        if (!verifyPassword(oldPassword, customer.getPassword())) {
            throw new Exception("Old password is incorrect");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new Exception("New password must be at least 6 characters long");
        }

        customer.setPassword(encodePassword(newPassword));
        customerRepository.save(customer);
        return true;
    }

    // Helper methods for password encoding/verification (simple implementation)
    private String encodePassword(String password) {
        // In production, use BCryptPasswordEncoder
        return java.util.Base64.getEncoder().encodeToString(password.getBytes());
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        // In production, use BCryptPasswordEncoder
        return java.util.Base64.getEncoder().encodeToString(rawPassword.getBytes()).equals(encodedPassword);
    }

    /**
     * Inner class to represent authentication session
     */
    public static class CustomAuthSession {
        private Long customerId;
        private String email;
        private String name;
        private String loginTime;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }
    }
}
