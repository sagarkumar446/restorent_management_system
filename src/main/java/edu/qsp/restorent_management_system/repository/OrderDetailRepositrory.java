package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.OrderDetail;

public interface  OrderDetailRepositrory extends JpaRepository<OrderDetail, Integer> {
    
}
