package com.project.shoply.service;

import com.project.shoply.entity.Brand;
import com.project.shoply.entity.Category;
import com.project.shoply.exception.GenericException;
import com.project.shoply.exception.ResourceNotFoundException;
import com.project.shoply.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public String saveCategory(String mame) {
        String categoryName = mame.trim().toLowerCase();
        if (categoryRepository.existsByName(categoryName))
            throw new GenericException("Category " + categoryName + " already exist",
                    HttpStatus.CONFLICT);
        Category category = new Category(categoryName);
        categoryRepository.save(category);
        return "Brand saved";
    }

    @Transactional
    public String updateCategory(String newName, long categoryId) {
        String newCategoryName = newName.trim().toLowerCase();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        if (categoryRepository.existsByNameAndIdNot(newCategoryName, categoryId ))
            throw new GenericException("Category " + newCategoryName + " already exist",
                    HttpStatus.CONFLICT);
        category.setName(newCategoryName);
        return "Brand update";
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    protected Category findCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "name", name));
    }
}
