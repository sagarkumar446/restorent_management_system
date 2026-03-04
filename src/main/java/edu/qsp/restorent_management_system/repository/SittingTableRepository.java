package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.qsp.restorent_management_system.model.SittingTable;
import java.util.List;
import java.util.Optional;

public interface SittingTableRepository extends JpaRepository<SittingTable, Long> {
    Optional<SittingTable> findByTableId(Long tableId);
    Optional<SittingTable> findByTableNumber(Integer tableNumber);
    List<SittingTable> findByStatus(String status);
    List<SittingTable> findAllByOrderByTableNumberAsc();
    
    @Query("SELECT t FROM SittingTable t WHERE t.status = 'AVAILABLE' AND t.seatingCapacity >= :numberOfGuests")
    List<SittingTable> findAvailableTablesByCapacity(@Param("numberOfGuests") Integer numberOfGuests);
}
