package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.qsp.restorent_management_system.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r WHERE r IN (SELECT res FROM Customer c JOIN c.reservation res WHERE c.customerId = :customerId) ORDER BY r.reservationDate DESC")
    List<Reservation> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT r FROM Reservation r WHERE r.sitting_table.tableId = :tableId AND r.reservationDate = :reservationDate")
    List<Reservation> findByTableAndDate(@Param("tableId") Long tableId, @Param("reservationDate") String reservationDate);
    
    Optional<Reservation> findByReservationId(Long reservationId);
}
