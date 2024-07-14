package edu.qsp.restorent_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.qsp.restorent_management_system.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    
}

