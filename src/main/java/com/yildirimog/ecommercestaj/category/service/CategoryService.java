package com.yildirimog.ecommercestaj.category.service;
import com.yildirimog.ecommercestaj.category.dto.CategoryCreateRequest;
import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.mapper.CategoryMapper;
import com.yildirimog.ecommercestaj.category.repository.CategoryRepository;
import com.yildirimog.ecommercestaj.common.Exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryResponse createCategory(CategoryCreateRequest dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());
        if (categoryRepository.existsByName(dto.name())){
            throw new IllegalArgumentException("Bu isimde bir kategori zaten var");
        }

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(saved.getId(), saved.getName());
    }
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }


    public Category getByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı"));
    }
    public CategoryResponse updateCategory(Long id,CategoryCreateRequest request){

        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Kategori bulunamadı"));

        category.setName(request.name());
        category.setDescription(request.description());
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new BusinessException("Bu isimde bir kategori zaten mevcut.");
        }
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Kategori bulunamadı");
        }
        categoryRepository.deleteById(id);
    }
}
