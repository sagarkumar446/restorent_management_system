package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.OrderBy;

public interface OrderRepositroy extends JpaRepository<OrderBy, Integer> {
    
}
