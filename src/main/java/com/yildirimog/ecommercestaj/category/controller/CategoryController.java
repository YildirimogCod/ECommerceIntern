package com.yildirimog.ecommercestaj.category.controller;

import com.yildirimog.ecommercestaj.category.dto.CategoryCreateRequest;
import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.mapper.CategoryMapper;
import com.yildirimog.ecommercestaj.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }
    @GetMapping
    public ResponseEntity <List<CategoryResponse>> findAllCategories(){
       List<CategoryResponse> categories = categoryService.getAllCategories();
       return ResponseEntity.ok(categories);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findCategoryById(@PathVariable Long id){
        Category category = categoryService.getByIdOrThrow(id);
        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        return ResponseEntity.ok(categoryResponse);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @PathVariable Long id,
                                                           @RequestBody CategoryCreateRequest request){

     CategoryResponse response = categoryService.updateCategory(id,request);
     return ResponseEntity.ok(response);

    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
