package edu.qsp.restorent_management_system.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import edu.qsp.restorent_management_system.dto.CustomerLoginRequest;
import edu.qsp.restorent_management_system.dto.CustomerLoginResponse;
import edu.qsp.restorent_management_system.dto.CustomerRegisterRequest;
import edu.qsp.restorent_management_system.dto.OrderRequest;
import edu.qsp.restorent_management_system.dto.ReservationRequest;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.model.OrderBy;
import edu.qsp.restorent_management_system.model.Reservation;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.service.CustomerAuthService;
import edu.qsp.restorent_management_system.service.CustomerAuthService.CustomAuthSession;
import edu.qsp.restorent_management_system.service.CustomerOrderService;
import edu.qsp.restorent_management_system.service.CustomerReservationService;
import edu.qsp.restorent_management_system.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerAuthService customerAuthService;
    
    @Autowired
    private CustomerOrderService customerOrderService;
    
    @Autowired
    private CustomerReservationService customerReservationService;

    // ==================== AUTHENTICATION ENDPOINTS ====================

    /**
     * Register a new customer
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> registerCustomer(@RequestBody CustomerRegisterRequest request) {
        try {
            Customer newCustomer = customerAuthService.registerCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(201, "Customer registered successfully", newCustomer, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Login customer
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomAuthSession>> loginCustomer(@RequestBody CustomerLoginRequest request) {
        try {
            CustomAuthSession session = customerAuthService.loginCustomer(request);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Login successful", session, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>(401, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Logout customer
     */
    @PostMapping("/{customerId}/logout")
    public ResponseEntity<ApiResponse<Boolean>> logoutCustomer(@PathVariable Long customerId) {
        try {
            boolean result = customerAuthService.logoutCustomer(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Logout successful", result, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get customer profile
     */
    @GetMapping("/{customerId}/profile")
    public ResponseEntity<ApiResponse<CustomerLoginResponse>> getCustomerProfile(@PathVariable Long customerId) {
        try {
            CustomerLoginResponse response = customerAuthService.getCustomerProfile(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Profile fetched successfully", response, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(404, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Update customer profile
     */
    @PutMapping("/{customerId}/profile")
    public ResponseEntity<ApiResponse<Customer>> updateCustomerProfile(
            @PathVariable Long customerId,
            @RequestBody CustomerRegisterRequest request) {
        try {
            Customer updatedCustomer = customerAuthService.updateCustomerProfile(customerId, request);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Profile updated successfully", updatedCustomer, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Change customer password
     */
    @PostMapping("/{customerId}/change-password")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(
            @PathVariable Long customerId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            boolean result = customerAuthService.changePassword(customerId, oldPassword, newPassword);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Password changed successfully", result, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Delete customer account
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCustomerAccount(@PathVariable Long customerId) {
        try {
            boolean result = customerService.deleteCustomerAccount(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Account deleted successfully", result, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    // ==================== MENU ENDPOINTS ====================

    /**
     * Get all menu items
     */
    @GetMapping("/menu")
    public ResponseEntity<ApiResponse<List<MenuItem>>> getAllMenuItems() {
        try {
            List<MenuItem> menuItems = customerService.getMenu();
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Menu items fetched successfully", menuItems, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get specific menu item
     */
    @GetMapping("/menu/{menuItemId}")
    public ResponseEntity<ApiResponse<MenuItem>> getMenuItemDetails(@PathVariable Integer menuItemId) {
        try {
            Optional<MenuItem> menuItem = customerService.getMenuItem(menuItemId);
            if (menuItem.isPresent()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "Menu item fetched successfully", menuItem.get(), true)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>(404, "Menu item not found", null, false)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, e.getMessage(), null, false)
            );
        }
    }

    // ==================== TABLE ENDPOINTS ====================

    /**
     * Get all available tables
     */
    @GetMapping("/tables")
    public ResponseEntity<ApiResponse<List<SittingTable>>> getAllTables() {
        try {
            List<SittingTable> tables = customerService.getAllTables();
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Tables fetched successfully", tables, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get available tables for a specific date and number of guests
     */
    @GetMapping("/tables/available")
    public ResponseEntity<ApiResponse<List<SittingTable>>> getAvailableTablesForDate(
            @RequestParam String date,
            @RequestParam Integer numberOfGuests,
            @RequestParam(required = false) String reservationTime) {
        try {
            List<SittingTable> availableTables = customerReservationService.getAvailableTablesForDate(date, numberOfGuests, reservationTime);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Available tables fetched successfully", availableTables, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    // ==================== ORDER ENDPOINTS ====================

    /**
     * Place a new order
     */
    @PostMapping("/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderBy>> placeOrder(
            @PathVariable Long customerId,
            @RequestBody OrderRequest orderRequest) {
        try {
            orderRequest.setCustomerId(customerId);
            OrderBy order = customerOrderService.placeOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(201, "Order placed successfully", order, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get all orders for a customer
     */
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<ApiResponse<List<OrderBy>>> getCustomerOrders(@PathVariable Long customerId) {
        try {
            List<OrderBy> orders = customerOrderService.getCustomerOrders(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Orders fetched successfully", orders, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get optimized order summary for customer
     */
    @GetMapping("/{customerId}/orders/summary")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCustomerOrderSummaries(@PathVariable Long customerId) {
        try {
            List<Map<String, Object>> orders = customerOrderService.getCustomerOrderSummaries(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order summaries fetched successfully", orders, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get specific order details
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderBy>> getOrderDetails(@PathVariable Long orderId) {
        try {
            OrderBy order = customerOrderService.getOrderDetails(orderId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order details fetched successfully", order, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(404, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Cancel an order
     */
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<Boolean>> cancelOrder(@PathVariable Long orderId) {
        try {
            boolean result = customerOrderService.cancelOrder(orderId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order cancelled successfully", result, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get order history with pagination
     */
    @GetMapping("/{customerId}/order-history")
    public ResponseEntity<ApiResponse<List<OrderBy>>> getOrderHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<OrderBy> orderHistory = customerOrderService.getOrderHistory(customerId, page, size);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Order history fetched successfully", orderHistory, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get total spending for a customer
     */
    @GetMapping("/{customerId}/total-spending")
    public ResponseEntity<ApiResponse<Double>> getTotalSpending(@PathVariable Long customerId) {
        try {
            double totalSpending = customerOrderService.getTotalSpending(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Total spending fetched successfully", totalSpending, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    // ==================== RESERVATION ENDPOINTS ====================

    /**
     * Make a table reservation
     */
    @PostMapping("/{customerId}/reservations")
    public ResponseEntity<ApiResponse<Reservation>> makeReservation(
            @PathVariable Long customerId,
            @RequestBody ReservationRequest reservationRequest) {
        try {
            reservationRequest.setCustomerId(customerId);
            Reservation reservation = customerReservationService.makeReservation(reservationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(201, "Reservation made successfully", reservation, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get all reservations for a customer
     */
    @GetMapping("/{customerId}/reservations")
    public ResponseEntity<ApiResponse<List<Reservation>>> getCustomerReservations(@PathVariable Long customerId) {
        try {
            List<Reservation> reservations = customerReservationService.getCustomerReservations(customerId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservations fetched successfully", reservations, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get specific reservation details
     */
    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<Reservation>> getReservationDetails(@PathVariable Long reservationId) {
        try {
            Reservation reservation = customerReservationService.getReservationDetails(reservationId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservation details fetched successfully", reservation, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(404, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Cancel a reservation
     */
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<Boolean>> cancelReservation(@PathVariable Long reservationId) {
        try {
            boolean result = customerReservationService.cancelReservation(reservationId);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservation cancelled successfully", result, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Modify a reservation
     */
    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<Reservation>> modifyReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationRequest reservationRequest) {
        try {
            Reservation modifiedReservation = customerReservationService.modifyReservation(reservationId, reservationRequest);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservation modified successfully", modifiedReservation, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    /**
     * Get reservation history with pagination
     */
    @GetMapping("/{customerId}/reservation-history")
    public ResponseEntity<ApiResponse<List<Reservation>>> getReservationHistory(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Reservation> reservationHistory = customerReservationService.getReservationHistory(customerId, page, size);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "Reservation history fetched successfully", reservationHistory, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(400, e.getMessage(), null, false)
            );
        }
    }

    // ==================== LOYALTY & POINTS ENDPOINTS ====================

    /**
     * Get loyalty points for a customer
     */
    @GetMapping("/{customerId}/loyalty-points")
    public ResponseEntity<ApiResponse<Double>> getLoyaltyPoints(@PathVariable Long customerId) {
        try {
            Optional<Customer> customer = customerService.getCustomerById(customerId);
            if (customer.isPresent()) {
                Double loyaltyPoints = customer.get().getLoyaltyPoints();
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "Loyalty points fetched successfully", loyaltyPoints, true)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>(404, "Customer not found", null, false)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(500, e.getMessage(), null, false)
            );
        }
    }
}
