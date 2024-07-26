package edu.qsp.restorent_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.Menu;
import edu.qsp.restorent_management_system.model.SittingTable;
import edu.qsp.restorent_management_system.repository.MenuRepository;
import edu.qsp.restorent_management_system.repository.SittingTableRepository;

@Service
public class CustomerService {
    @Autowired
    MenuRepository menurepository;
    @Autowired
    SittingTableRepository sittingTableRespository;

    public List<Menu> getMenu(){
        return menurepository.findAll();
    }
    public Optional<Menu> getMenuItem(Integer id){
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
    
}
