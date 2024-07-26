 package edu.qsp.restorent_management_system.controller;
import java.util.List;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.model.Menu;
import edu.qsp.restorent_management_system.service.CustomerService;
 @RestController
 public class CustomerController {

    @Autowired
    CustomerService customerService;
    @Autowired
    ResponseStructure<List<Menu>> responseStructureMenu;
   
    
    @GetMapping("menu_item")
   
    public ResponseStructure<List<Menu>> ViewAllMenusItems() {

        if(customerService.getMenu()!=null)
        {
            responseStructureMenu.setData(customerService.getMenu());
            responseStructureMenu.setStatusCode(204);
            responseStructureMenu.setMessage("data fetched successfully");
       
            return  responseStructureMenu;

        }
        else{
            responseStructureMenu.setData(customerService.getMenu());
            responseStructureMenu.setMessage("no data found");
            responseStructureMenu.setStatusCode(200);
        
            return  responseStructureMenu;

        }
        
       

    }
 }
