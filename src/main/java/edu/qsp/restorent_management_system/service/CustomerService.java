package edu.qsp.restorent_management_system.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.repository.CustomerRepository;
import edu.qsp.restorent_management_system.repository.MenuRepository;
import edu.qsp.restorent_management_system.repository.SittingTableRepository;

@Service
public class CustomerService {
    @Autowired
    MenuRepository menurepository;
    @Autowired
    SittingTableRepository sittingTableRespository;
    @Autowired
    CustomerRepository customerRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<MenuItem> getMenu() {
        return menurepository.findAll();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<MenuItem> getMenuItem(Integer id) {
        return menurepository.findById(Long.valueOf(id));
    }

    public List<SittingTable> getAllTables() {
        return sittingTableRespository.findAllByOrderByTableNumberAsc();
    }

    public Optional<SittingTable> getTable(Integer id) {
        return sittingTableRespository.findById((long) id);
    }

    public SittingTable createTable(Integer tableNumber, Integer seatingCapacity, String status) throws Exception {
        if (tableNumber == null || tableNumber <= 0) {
            throw new Exception("Table number must be greater than 0");
        }
        if (seatingCapacity == null || seatingCapacity <= 0) {
            throw new Exception("Seating capacity must be greater than 0");
        }
        if (sittingTableRespository.findByTableNumber(tableNumber).isPresent()) {
            throw new Exception("Table number already exists");
        }

        SittingTable table = new SittingTable();
        table.setTableNumber(tableNumber);
        table.setSeatingCapacity(seatingCapacity);
        table.setStatus(status == null || status.isBlank() ? "AVAILABLE" : status.toUpperCase());
        return sittingTableRespository.save(table);
    }

    public SittingTable updateTableStatus(Long tableId, String status) throws Exception {
        if (tableId == null) {
            throw new Exception("Table id is required");
        }
        Optional<SittingTable> optionalTable = sittingTableRespository.findByTableId(tableId);
        if (!optionalTable.isPresent()) {
            throw new Exception("Table not found");
        }
        if (status == null || status.isBlank()) {
            throw new Exception("Status is required");
        }

        SittingTable table = optionalTable.get();
        table.setStatus(status.toUpperCase());
        return sittingTableRespository.save(table);
    }

    public Boolean setCutomer(Customer customer) {
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }

    /**
     * Get customer by ID
     */
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    /**
     * Get customer by email
     */
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Delete customer account
     */
    public boolean deleteCustomerAccount(Long customerId) throws Exception {
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerId(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new Exception("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        customer.setIsActive(false);
        customerRepository.save(customer);
        return true;
    }

    /**
     * Get all active customers
     */
    public List<Customer> getAllActiveCustomers() {
        return customerRepository.findByIsActive(true);
    }

    /**
     * Search menu items by name
     */
    public List<MenuItem> searchMenuItems(String searchTerm) {
        // This would require a custom repository method
        // For now, returning all items
        return menurepository.findAll();
    }
}
