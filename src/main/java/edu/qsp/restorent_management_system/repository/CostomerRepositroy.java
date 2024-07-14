package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.Customer;

public interface  CostomerRepositroy extends JpaRepository<Customer, Integer> {
    
}
