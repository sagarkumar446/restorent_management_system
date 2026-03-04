package edu.qsp.restorent_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.qsp.restorent_management_system.model.OrderBy;

public interface OrderRepository extends JpaRepository<OrderBy, Long> {

    @Query("SELECT o FROM OrderBy o WHERE o IN (SELECT ord FROM Customer c JOIN c.orders ord WHERE c.customerId = :customerId) ORDER BY o.orderDate DESC")
    List<OrderBy> findByCustomerId(@Param("customerId") Long customerId);

    Optional<OrderBy> findByOrderId(Long orderId);

    List<OrderBy> findTop10ByOrderByOrderIdDesc();
}
