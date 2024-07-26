package edu.qsp.restorent_management_system.Configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.qsp.restorent_management_system.model.Menu;

/**
 * Configuration class for defining Spring beans.
 */
@Configuration
public class AppConfigure {


    @Bean
    public ResponseStructure<List<Menu>> responseStructureMenu() {
        return new ResponseStructure<>();
    }
    
    // Additional bean definitions can be added here
}
