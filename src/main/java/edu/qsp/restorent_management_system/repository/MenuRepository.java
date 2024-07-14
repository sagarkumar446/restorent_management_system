package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.qsp.restorent_management_system.model.Menu;

public interface  MenuRepository extends JpaRepository<Menu , Integer>{
    
}
