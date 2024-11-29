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

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.model.Customer;
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
    @PostMapping("/addMenuItem")
    public ResponseEntity<ResponseStructure<MenuItem>> addMenuItem(
        @RequestParam("itemName") String itemName,
        @RequestParam("description") String description,
        @RequestParam("price") Double price,
        @RequestParam("category") String category,
        @RequestParam("veg") Boolean veg,
        @RequestParam("image") MultipartFile image){
        responseStructureMenuItem.setData(employeeService.addMenuItem( itemName,description,price, category,veg,image));
        return new  ResponseEntity<>(responseStructureMenuItem, HttpStatus.ACCEPTED);
    }
    @GetMapping("/getAllCustomers")
    public ResponseEntity<ResponseStructure<List<Customer>>> getALlCustomers (@RequestParam String param) {


        return new ResponseEntity<>(responseStructureCustomers,HttpStatus.ACCEPTED);
    }
}
    