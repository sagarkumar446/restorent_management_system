package edu.qsp.restorent_management_system.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.qsp.restorent_management_system.model.MenuItem;
import edu.qsp.restorent_management_system.repository.MenuRepository;


@Service
public class EmployeeService {
    @Autowired
    MenuRepository menuRepository;
    public  MenuItem addMenuItem(String itemName, String description, Double price,String category,Boolean veg, MultipartFile image){
         MenuItem menuItem= new MenuItem();
         menuItem.setCategory(category);
         menuItem.setDescription(description);
         menuItem.setItemName(itemName);
         menuItem.setPrice(price);
         menuItem.setVeg(veg);
        try {
            menuItem.setImage(image.getBytes());
            menuRepository.save(menuItem);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


         
         return menuItem;
          
    }








    
}
