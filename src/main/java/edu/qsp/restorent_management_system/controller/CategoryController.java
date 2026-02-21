package edu.qsp.restorent_management_system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.qsp.restorent_management_system.config.ResponseStructure;
import edu.qsp.restorent_management_system.model.Category;
import edu.qsp.restorent_management_system.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ResponseStructure<List<Category>> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Categories fetched successfully");
        response.setData(categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String displayName = body.getOrDefault("displayName", name);
            String emoji = body.getOrDefault("emoji", "🍽️");
            Category created = categoryService.addCategory(name, displayName, emoji);
            ResponseStructure<Category> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Category created successfully");
            response.setData(created);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ResponseStructure<String> response = new ResponseStructure<>();
            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setMessage(e.getMessage());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Category deleted successfully");
        response.setData("Deleted id: " + id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
