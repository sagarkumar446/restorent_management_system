package edu.qsp.restorent_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.qsp.restorent_management_system.config.ResponseStructure;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.Employee;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.service.EmployeeService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ResponseStructure<MenuItem> responseStructureMenuItem;
    @Autowired
    ResponseStructure<List<Customer>> responseStructureCustomers;
    @Autowired
    ResponseStructure<Employee> responseStructureEmployee;

    @PostMapping("/menu-items")
    public ResponseEntity<ResponseStructure<MenuItem>> addMenuItem(
            @RequestParam("itemName") String itemName,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("veg") Boolean veg,
            @RequestParam("image") MultipartFile image) {
        responseStructureMenuItem
                .setData(employeeService.addMenuItem(itemName, description, price, category, veg, image));
        return new ResponseEntity<>(responseStructureMenuItem, HttpStatus.ACCEPTED);
    }

    @GetMapping("/customers")
    public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers(@RequestParam String param) {

        return new ResponseEntity<>(responseStructureCustomers, HttpStatus.ACCEPTED);
    }

    @PostMapping("/employee/login")
    public ResponseEntity<ResponseStructure<Employee>> login(@RequestParam String email,
            @RequestParam String password) {
        Employee employee = employeeService.login(email, password);
        if (employee != null) {
            responseStructureEmployee.setData(employee);
            responseStructureEmployee.setMessage("Login successful");
            responseStructureEmployee.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(responseStructureEmployee, HttpStatus.OK);
        } else {
            responseStructureEmployee.setData(null);
            responseStructureEmployee.setMessage("Invalid email or password");
            responseStructureEmployee.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(responseStructureEmployee, HttpStatus.UNAUTHORIZED);
        }
    }
}
