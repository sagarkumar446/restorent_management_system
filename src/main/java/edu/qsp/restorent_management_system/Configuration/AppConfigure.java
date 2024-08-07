package edu.qsp.restorent_management_system.Configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.model.OrderBy;
import edu.qsp.restorent_management_system.model.SittingTable;

/**
 * Configuration class for defining Spring beans.
 */
@Configuration
public class AppConfigure {


    @Bean
    public ResponseStructure<List<MenuItem>> responseStructureMenu() {
        return new ResponseStructure<>();
    }
    @Bean
    public ResponseStructure<List<SittingTable>> responseStructureTables(){
        return new ResponseStructure<>();
    }
    @Bean
    public ResponseStructure<SittingTable> responseStructureTable(){
        return new ResponseStructure<>();
    }
    @Bean
    public ResponseStructure<Integer> responseStructureInteger(){
        return new ResponseStructure<>();
    }
    @Bean
    public ResponseStructure<OrderBy> responseStructureOrder(){
        return new ResponseStructure<>();
        
    }
    @Bean 
    public ResponseStructure<Customer> responseStructureCustomer(){
        return  new ResponseStructure<>();
    }
    
    
    // Additional bean definitions can be added here
}
