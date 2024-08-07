package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.Customer;

public interface  CustomerRepositroy extends JpaRepository<Customer, Integer> {
    
}
