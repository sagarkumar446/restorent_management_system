package edu.qsp.restorent_management_system.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.qsp.restorent_management_system.Configuration.ResponseStructure;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.service.CustomerService;




@RestController
@RequestMapping("view_table")
public class TableController {

    @Autowired
    CustomerService customerService;
    
    @Autowired
    ResponseStructure<List<SittingTable>> responseStructure_sittingTables;
    @Autowired
    ResponseStructure<SittingTable> responseStructure_sittingTable;
    @GetMapping("")
    public ResponseEntity<ResponseStructure<List<SittingTable>>> viewAllTables() {
     
        if(customerService.getAllTables()!=null)
        {
            responseStructure_sittingTables.setData(customerService.getAllTables());
            responseStructure_sittingTables.setMessage("All data fetch sucessfully");
            responseStructure_sittingTables.setStatusCode(200);
            return new  ResponseEntity<>(responseStructure_sittingTables,HttpStatus.ACCEPTED);


        }
        else{
            responseStructure_sittingTables.setData(null);
            responseStructure_sittingTables.setMessage("data not found sorry");
            responseStructure_sittingTables.setStatusCode(204);
            return new  ResponseEntity<>(responseStructure_sittingTables,HttpStatus.EXPECTATION_FAILED);


        }
        
 
    }
    @GetMapping("{id}")
    public ResponseStructure<SittingTable> putMethodName(@PathVariable Integer id) {
        java.util.Optional<SittingTable> opt= customerService.getTable(id);
        if(opt.isPresent())
        {
            responseStructure_sittingTable.setData(opt.get());
            responseStructure_sittingTable.setMessage("data fetch sucessfully");
            responseStructure_sittingTable.setStatusCode(200);
            return  responseStructure_sittingTable;


        }
        else{
            responseStructure_sittingTable.setData(null);
            responseStructure_sittingTable.setMessage("data not found sorry");
            responseStructure_sittingTable.setStatusCode(204);
            return  responseStructure_sittingTable;


        }
        
        
       
       
    }

    
}
