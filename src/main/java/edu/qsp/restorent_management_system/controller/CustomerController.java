 package edu.qsp.restorent_management_system.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.service.CustomerService;



 @RestController
 public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    ResponseStructure<List<MenuItem>> responseStructureMenu;
    @Autowired
    ResponseStructure<Customer> responseStructureCustomer;
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
    @PostMapping("table_reserve/{customer_id}/{table_id}")
    public String postMethodName(@PathVariable Integer customer_id,@PathVariable Integer table_id) {
        
        
        return " "+customer_id;
    }
    @PostMapping("add_customer")
    public ResponseEntity<ResponseStructure<Customer>> postMethodName(@RequestBody Customer entity) {

         if(customerService.setCutomer(entity))
         {    responseStructureCustomer.setMessage("customer added sucsessfully");
         responseStructureCustomer.setStatusCode(200);
         responseStructureCustomer.setData(entity);

        
        return new ResponseEntity<>(responseStructureCustomer,HttpStatus.ACCEPTED);

         }
         else
         {
        responseStructureCustomer.setMessage("operation faild");
        responseStructureCustomer.setStatusCode(200);
        responseStructureCustomer.setData(null);
        return new ResponseEntity<>(responseStructureCustomer,HttpStatus.EXPECTATION_FAILED);
         }
     
    }

    
    
    
   
    
    
    

 }
