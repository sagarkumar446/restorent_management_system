package edu.qsp.restorent_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.Customer;
import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.repository.CustomerRepositroy;
import edu.qsp.restorent_management_system.repository.MenuRepository;
import edu.qsp.restorent_management_system.repository.SittingTableRepository;

@Service
public class CustomerService {
    @Autowired
    MenuRepository menurepository;
    @Autowired
    SittingTableRepository sittingTableRespository;
    @Autowired
    CustomerRepositroy customerRepositroy;


    public List<MenuItem> getMenu(){
        return menurepository.findAll();
    }
    public Optional<MenuItem> getMenuItem(Integer id){
        return  menurepository.findById(id);
    }
    public List<SittingTable> getAllTables()
    {
        return sittingTableRespository.findAll();
    }
    public Optional<SittingTable> getTable(Integer id)
    {
    
        return sittingTableRespository.findById(id);
    }
    public Boolean setCutomer(Customer customer)
    {
        try {
            customerRepositroy.save(customer);
            
            
        } catch (Exception e) {

            System.err.println(e);
            return false;
        }
        return true;
    }
   
   
    
    
}
