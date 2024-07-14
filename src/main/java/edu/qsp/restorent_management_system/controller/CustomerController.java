package edu.qsp.restorent_management_system.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.response_structure.ResponseStructure;




@RestController
public class CustomerController {
   @GetMapping("path")
   public ResponseStructure<Customer> getMethodName(@RequestParam String param) {
       return null;
   }
   
    
    
}
