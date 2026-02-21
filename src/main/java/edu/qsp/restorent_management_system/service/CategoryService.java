package edu.qsp.restorent_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.qsp.restorent_management_system.model.Category;
import edu.qsp.restorent_management_system.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(String name, String displayName, String emoji) {
        String upperName = name.toUpperCase().trim().replace(" ", "_");
        if (categoryRepository.existsByName(upperName)) {
            throw new IllegalArgumentException("Category '" + upperName + "' already exists.");
        }
        Category category = new Category(upperName, displayName, emoji);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
