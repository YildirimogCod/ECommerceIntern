package com.yildirimog.ecommercestaj.product.service;

import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.repository.CategoryRepository;
import com.yildirimog.ecommercestaj.product.dto.ProductCreateRequest;
import com.yildirimog.ecommercestaj.product.dto.ProductResponse;
import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.product.mapper.ProductMapper;
import com.yildirimog.ecommercestaj.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductCreateRequest dto) {
        Product product = productMapper.toEntity(dto);

        // Kategori set et (tek kategori)
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        product.setCategories(Set.of(category));

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

}
