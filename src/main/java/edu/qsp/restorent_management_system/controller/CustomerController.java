 package edu.qsp.restorent_management_system.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.service.CustomerService;

 @RestController
 public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ResponseStructure<List<MenuItem>> responseStructureMenu; 
    @GetMapping("menu_item")
    public ResponseEntity<ResponseStructure<List<MenuItem>>> ViewAllMenusItems() {
        if(customerService.getMenu()!=null)
        { 
            responseStructureMenu.setData(customerService.getMenu());
            responseStructureMenu.setStatusCode(HttpStatus.ACCEPTED.value());
            responseStructureMenu.setMessage("data fetched successfully");
       
            return   new ResponseEntity<>(responseStructureMenu,HttpStatus.ACCEPTED);
        }
        else{
            responseStructureMenu.setData(customerService.getMenu());
            responseStructureMenu.setMessage("no data found");
            responseStructureMenu.setStatusCode(HttpStatus.EXPECTATION_FAILED.value());
        
            return new  ResponseEntity<>(responseStructureMenu,HttpStatus.ALREADY_REPORTED);

        }
    }
    @PostMapping("path")
    public ResponseEntity<ResponseStructure<List<MenuItem>>> postMethodName(@RequestBody List<MenuItem> items) {

        
        return null;
        //TODO: process POST request
        
     
    }
    

 }
