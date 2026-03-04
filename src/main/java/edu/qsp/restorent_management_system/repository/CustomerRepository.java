package edu.qsp.restorent_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.qsp.restorent_management_system.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByContactNumber(String contactNumber);
    Optional<Customer> findByCustomerId(Long customerId);
    List<Customer> findByIsActive(Boolean isActive);
    
    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.password = :password")
    Optional<Customer> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}
