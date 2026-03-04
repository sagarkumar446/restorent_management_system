package edu.qsp.restorent_management_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.config.ResponseStructure;
import edu.qsp.restorent_management_system.dto.ApiResponse;
import edu.qsp.restorent_management_system.model.Reservation;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.service.CustomerReservationService;
import edu.qsp.restorent_management_system.service.CustomerService;

@RestController
@RequestMapping("/api/view-tables")
@CrossOrigin(origins = "http://localhost:3000")
public class TableController {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerReservationService customerReservationService;

    @Autowired
    ResponseStructure<List<SittingTable>> responseStructure_sittingTables;
    @Autowired
    ResponseStructure<SittingTable> responseStructure_sittingTable;

    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<List<SittingTable>>> viewAllTables() {

        if (customerService.getAllTables() != null) {
            responseStructure_sittingTables.setData(customerService.getAllTables());
            responseStructure_sittingTables.setMessage("All data fetch sucessfully");
            responseStructure_sittingTables.setStatusCode(200);
            return new ResponseEntity<>(responseStructure_sittingTables, HttpStatus.ACCEPTED);

        } else {
            responseStructure_sittingTables.setData(null);
            responseStructure_sittingTables.setMessage("data not found sorry");
            responseStructure_sittingTables.setStatusCode(204);
            return new ResponseEntity<>(responseStructure_sittingTables, HttpStatus.EXPECTATION_FAILED);

        }

    }

    @GetMapping("/{id}")
    public ResponseStructure<SittingTable> getTableById(@PathVariable Integer id) {
        java.util.Optional<SittingTable> opt = customerService.getTable(id);
        if (opt.isPresent()) {
            responseStructure_sittingTable.setData(opt.get());
            responseStructure_sittingTable.setMessage("data fetch sucessfully");
            responseStructure_sittingTable.setStatusCode(200);
            return responseStructure_sittingTable;

        } else {
            responseStructure_sittingTable.setData(null);
            responseStructure_sittingTable.setMessage("data not found sorry");
            responseStructure_sittingTable.setStatusCode(204);
            return responseStructure_sittingTable;

        }

    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<SittingTable>>> getAvailableTables(
            @RequestParam String date,
            @RequestParam Integer numberOfGuests,
            @RequestParam(required = false) String reservationTime) {
        try {
            List<SittingTable> availableTables =
                    customerReservationService.getAvailableTablesForDate(date, numberOfGuests, reservationTime);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Available tables fetched successfully", availableTables, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<SittingTable>> createTable(@RequestBody Map<String, Object> body) {
        try {
            Integer tableNumber = Integer.valueOf(String.valueOf(body.get("tableNumber")));
            Integer seatingCapacity = Integer.valueOf(String.valueOf(body.get("seatingCapacity")));
            String status = body.get("status") == null ? "AVAILABLE" : String.valueOf(body.get("status"));

            SittingTable table = customerService.createTable(tableNumber, seatingCapacity, status);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(201, "Table created successfully", table, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    @PutMapping("/admin/{tableId}/status")
    public ResponseEntity<ApiResponse<SittingTable>> updateTableStatus(
            @PathVariable Long tableId,
            @RequestParam String status) {
        try {
            SittingTable table = customerService.updateTableStatus(tableId, status);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Table status updated successfully", table, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllReservationsForAdmin() {
        try {
            List<Map<String, Object>> reservations = customerReservationService.getAllReservationsForAdmin();
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservations fetched successfully", reservations, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, e.getMessage(), null, false)
            );
        }
    }

    @PutMapping("/admin/reservations/{reservationId}/status")
    public ResponseEntity<ApiResponse<Reservation>> updateReservationStatus(
            @PathVariable Long reservationId,
            @RequestParam String status) {
        try {
            Reservation reservation = customerReservationService.updateReservationStatus(reservationId, status);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservation status updated successfully", reservation, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

}
