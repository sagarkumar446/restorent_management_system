package edu.qsp.restorent_management_system.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.service.CustomerService;

@RestController
@RequestMapping("view_table")
public class TableController {

    @Autowired
    CustomerService customerService;
    @GetMapping("")
    public java.util.List<SittingTable> viewAllTables() {
        return customerService.getAllTables();
    }
    @GetMapping("{id}")
    public SittingTable putMethodName(@PathVariable Integer id) {

        
        java.util.Optional<SittingTable> opt= customerService.getTable(id);
       return  opt.isPresent()?opt.get():null;
       
    }
    
}
