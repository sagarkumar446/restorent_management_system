package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.Order;

public interface OrderRepositroy extends JpaRepository<Order, Integer> {
    
}
