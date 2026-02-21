package edu.qsp.restorent_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.qsp.restorent_management_system.model.Category;
import edu.qsp.restorent_management_system.model.Employee;
import edu.qsp.restorent_management_system.repository.CategoryRepository;
import edu.qsp.restorent_management_system.repository.EmployeeRepository;

@SpringBootApplication
public class RestaurantManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementSystemApplication.class, args);
        System.err.println("i am in spring boot");
    }

    @Bean
    public CommandLineRunner seedAdmin(EmployeeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Employee admin = new Employee();
                admin.setName("Main Admin");
                admin.setEmail("admin@tfc.com");
                admin.setPassword("admin");
                admin.setRole("ADMIN");
                admin.setContact(9876543210L);
                admin.setSalary(50000.0);
                repository.save(admin);
                System.out.println("Default admin created: admin@tfc.com / admin");
            }
        };
    }

    @Bean
    public CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("GRAINS", "Grains & Main Course", "🍛"));
                categoryRepository.save(new Category("SNACKS", "Snacks & Starters", "🍟"));
                categoryRepository.save(new Category("DESSERTS", "Desserts", "🍰"));
                categoryRepository.save(new Category("BEVERAGES", "Beverages", "🥤"));
                categoryRepository.save(new Category("MEAT", "Meat Specialities", "🍖"));
                categoryRepository.save(new Category("SEAFOOD", "Seafood", "🦐"));
                System.out.println("Default categories seeded.");
            }
        };
    }
}