package edu.qsp.restorent_management_system.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.dto.ReservationRequest;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.Reservation;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.repository.CustomerRepository;
import edu.qsp.restorent_management_system.repository.ReservationRepository;
import edu.qsp.restorent_management_system.repository.SittingTableRepository;

@Service
public class CustomerReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SittingTableRepository sittingTableRepository;

    /**
     * Make a table reservation
     */
    public Reservation makeReservation(ReservationRequest reservationRequest) throws Exception {
        if (reservationRequest.getTableId() == null) {
            throw new Exception("Table is required");
        }
        if (reservationRequest.getReservationDate() == null || reservationRequest.getReservationDate().isBlank()) {
            throw new Exception("Reservation date is required");
        }
        if (reservationRequest.getReservationTime() == null || reservationRequest.getReservationTime().isBlank()) {
            throw new Exception("Reservation time is required");
        }
        if (reservationRequest.getNumberOfGuests() == null || reservationRequest.getNumberOfGuests() <= 0) {
            throw new Exception("Number of guests must be greater than 0");
        }

        // Validate customer
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(reservationRequest.getCustomerId());
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();

        // Validate table
        Optional<SittingTable> optionalTable = sittingTableRepository.findByTableId(reservationRequest.getTableId());
        if (!optionalTable.isPresent()) {
            throw new Exception("Table not found");
        }

        SittingTable table = optionalTable.get();
        if (table.getStatus() != null && !"AVAILABLE".equalsIgnoreCase(table.getStatus())) {
            throw new Exception("Selected table is not available right now");
        }

        // Check table capacity
        if (reservationRequest.getNumberOfGuests() > table.getSeatingCapacity()) {
            throw new Exception("Number of guests exceeds table capacity");
        }

        // Check if table is already reserved for this date and time
        List<Reservation> existingReservations = reservationRepository.findByTableAndDate(
                reservationRequest.getTableId(), reservationRequest.getReservationDate());
        
        for (Reservation res : existingReservations) {
            if (res.getReservationTime().equals(reservationRequest.getReservationTime())) {
                throw new Exception("Table is already reserved for this date and time");
            }
        }

        // Create new reservation
        Reservation newReservation = new Reservation();
        newReservation.setReservationDate(reservationRequest.getReservationDate());
        newReservation.setReservationTime(reservationRequest.getReservationTime());
        newReservation.setNumberOfGuests(reservationRequest.getNumberOfGuests());
        newReservation.setSpecialRequests(reservationRequest.getSpecialRequests());
        newReservation.setReservationStatus("CONFIRMED");
        newReservation.setSitting_table(table);

        // Save reservation
        Reservation savedReservation = reservationRepository.save(newReservation);

        // Add reservation to customer's reservation list
        if (customer.getreservation() == null) {
            customer.setreservation(new ArrayList<>());
        }
        customer.getreservation().add(savedReservation);
        customerRepository.save(customer);

        return savedReservation;
    }

    /**
     * Get all reservations for a customer
     */
    public List<Reservation> getCustomerReservations(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        return reservationRepository.findByCustomerId(customerId);
    }

    /**
     * Get specific reservation details
     */
    public Reservation getReservationDetails(Long reservationId) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);
        if (!optionalReservation.isPresent()) {
            throw new Exception("Reservation not found");
        }

        return optionalReservation.get();
    }

    /**
     * Cancel a reservation
     */
    public boolean cancelReservation(Long reservationId) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);
        if (!optionalReservation.isPresent()) {
            throw new Exception("Reservation not found");
        }

        reservationRepository.delete(optionalReservation.get());
        return true;
    }

    /**
     * Modify reservation date/time
     */
    public Reservation modifyReservation(Long reservationId, ReservationRequest reservationRequest) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);
        if (!optionalReservation.isPresent()) {
            throw new Exception("Reservation not found");
        }

        Reservation reservation = optionalReservation.get();

        // Validate new date/time doesn't conflict with other reservations
        List<Reservation> existingReservations = reservationRepository.findByTableAndDate(
                reservation.getSitting_table().getTableId(), reservationRequest.getReservationDate());

        for (Reservation res : existingReservations) {
            if (res.getReservationTime().equals(reservationRequest.getReservationTime()) 
                    && !res.getReservationId().equals(reservationId)) {
                throw new Exception("Table is already reserved for this date and time");
            }
        }

        reservation.setReservationDate(reservationRequest.getReservationDate());
        reservation.setReservationTime(reservationRequest.getReservationTime());
        if (reservationRequest.getNumberOfGuests() != null) {
            reservation.setNumberOfGuests(reservationRequest.getNumberOfGuests());
        }
        reservation.setSpecialRequests(reservationRequest.getSpecialRequests());

        return reservationRepository.save(reservation);
    }

    /**
     * Get available tables for a date and number of guests
     */
    public List<SittingTable> getAvailableTablesForDate(String date, Integer numberOfGuests) throws Exception {
        return getAvailableTablesForDate(date, numberOfGuests, null);
    }

    public List<SittingTable> getAvailableTablesForDate(String date, Integer numberOfGuests, String reservationTime) throws Exception {
        if (numberOfGuests == null || numberOfGuests <= 0) {
            throw new Exception("Number of guests must be greater than 0");
        }
        if (date == null || date.isBlank()) {
            throw new Exception("Date is required");
        }

        // Get all tables with sufficient capacity
        List<SittingTable> availableTables = sittingTableRepository.findAvailableTablesByCapacity(numberOfGuests);
        
        // Filter out already reserved tables for the given date
        List<SittingTable> finalAvailableTables = new ArrayList<>();
        
        for (SittingTable table : availableTables) {
            List<Reservation> reservationsForTable = reservationRepository.findByTableAndDate(table.getTableId(), date);
            boolean isConflict = false;
            for (Reservation reservation : reservationsForTable) {
                if ("CANCELLED".equalsIgnoreCase(reservation.getReservationStatus())) {
                    continue;
                }

                if (reservationTime == null || reservationTime.isBlank()) {
                    isConflict = true;
                    break;
                }

                if (reservationTime.equals(reservation.getReservationTime())) {
                    isConflict = true;
                    break;
                }
            }

            if (!isConflict) {
                finalAvailableTables.add(table);
            }
        }

        return finalAvailableTables;
    }

    public List<Map<String, Object>> getAllReservationsForAdmin() {
        List<Customer> customers = customerRepository.findAll();
        List<Map<String, Object>> reservations = new ArrayList<>();
        Set<Long> seenReservationIds = new HashSet<>();

        for (Customer customer : customers) {
            if (customer.getreservation() == null) continue;

            for (Reservation reservation : customer.getreservation()) {
                if (reservation == null || reservation.getReservationId() == null) continue;
                if (seenReservationIds.contains(reservation.getReservationId())) continue;
                seenReservationIds.add(reservation.getReservationId());

                Map<String, Object> reservationData = new HashMap<>();
                reservationData.put("reservationId", reservation.getReservationId());
                reservationData.put("reservationDate", reservation.getReservationDate());
                reservationData.put("reservationTime", reservation.getReservationTime());
                reservationData.put("numberOfGuests", reservation.getNumberOfGuests());
                reservationData.put("specialRequests", reservation.getSpecialRequests());
                reservationData.put("reservationStatus", reservation.getReservationStatus());

                if (reservation.getSitting_table() != null) {
                    reservationData.put("tableId", reservation.getSitting_table().getTableId());
                    reservationData.put("tableNumber", reservation.getSitting_table().getTableNumber());
                    reservationData.put("seatingCapacity", reservation.getSitting_table().getSeatingCapacity());
                    reservationData.put("tableStatus", reservation.getSitting_table().getStatus());
                } else {
                    reservationData.put("tableId", null);
                    reservationData.put("tableNumber", null);
                    reservationData.put("seatingCapacity", null);
                    reservationData.put("tableStatus", null);
                }

                reservationData.put("customerId", customer.getCustomerId());
                reservationData.put("customerName", customer.getName());
                reservationData.put("customerEmail", customer.getEmail());
                reservationData.put("customerContact", customer.getContactNumber());

                reservations.add(reservationData);
            }
        }

        reservations.sort(
                Comparator.<Map<String, Object>, String>comparing(
                        r -> String.valueOf(r.get("reservationDate")),
                        Comparator.nullsLast(String::compareTo))
                        .reversed()
                        .thenComparing(
                                r -> String.valueOf(r.get("reservationTime")),
                                Comparator.nullsLast(String::compareTo))
                        .reversed()
                        .thenComparing(
                                r -> Long.parseLong(String.valueOf(r.get("reservationId"))),
                                Comparator.reverseOrder()));

        return reservations;
    }

    public Reservation updateReservationStatus(Long reservationId, String reservationStatus) throws Exception {
        Optional<Reservation> optionalReservation = reservationRepository.findByReservationId(reservationId);
        if (!optionalReservation.isPresent()) {
            throw new Exception("Reservation not found");
        }
        if (reservationStatus == null || reservationStatus.isBlank()) {
            throw new Exception("Reservation status is required");
        }

        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(reservationStatus.toUpperCase());
        return reservationRepository.save(reservation);
    }

    /**
     * Get reservation history with pagination
     */
    public List<Reservation> getReservationHistory(Long customerId, int page, int size) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        List<Reservation> allReservations = reservationRepository.findByCustomerId(customerId);
        
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allReservations.size());
        
        if (startIndex >= allReservations.size()) {
            return new ArrayList<>();
        }

        return allReservations.subList(startIndex, endIndex);
    }
}
