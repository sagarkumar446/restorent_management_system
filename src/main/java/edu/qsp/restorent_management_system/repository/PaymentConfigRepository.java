package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.qsp.restorent_management_system.model.PaymentConfig;

@Repository
public interface PaymentConfigRepository extends JpaRepository<PaymentConfig, Integer> {
}
