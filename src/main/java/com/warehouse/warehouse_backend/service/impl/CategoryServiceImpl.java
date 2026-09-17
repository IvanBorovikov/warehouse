package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.category.CategoryDTO;
import com.warehouse.warehouse_backend.dto.category.CategoryUpdateDTO;
import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.Category;
import com.warehouse.warehouse_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryDTO> findAllCategories(){
        List<Category> listCategory = categoryRepository.findAll();
        List<CategoryDTO> listDtoCategory = new ArrayList<>();
        for (Category dto : listCategory) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(dto.getId());
            categoryDTO.setName(dto.getName());
            categoryDTO.setDescription(dto.getDescription());
            listDtoCategory.add(categoryDTO);
        }

        return listDtoCategory;
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepository.save(category);

        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }


    public CategoryDTO findCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(""));

        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO){
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(""));
        if (categoryUpdateDTO.getName() != null) {
            category.setName(categoryUpdateDTO.getName());
        }

        if (categoryUpdateDTO.getDescription() != null) {
            category.setDescription(categoryUpdateDTO.getDescription());
        }

        categoryRepository.save(category);

        return new CategoryDTO(category.getId(), categoryUpdateDTO.getName(), categoryUpdateDTO.getDescription());
    }

    public void deleteCategoryById(Long id){
        if (!categoryRepository.existsById(id)){
            throw new NotFoundException("");
        }
        categoryRepository.deleteById(id);
    }

}
