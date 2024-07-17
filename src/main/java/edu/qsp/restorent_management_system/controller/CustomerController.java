package edu.qsp.restorent_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.model.Menu;
import edu.qsp.restorent_management_system.service.CustomerService;
@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;
   
    @GetMapping("menu_item")
    public List<Menu> ViewAllMenusItems() {
        return customerService.getMenu();
         
    }
 
    
  
    
    

    
    
}
