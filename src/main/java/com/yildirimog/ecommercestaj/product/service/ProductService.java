package com.yildirimog.ecommercestaj.product.service;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.repository.CategoryRepository;
import com.yildirimog.ecommercestaj.product.dto.ProductCreateRequest;
import com.yildirimog.ecommercestaj.product.dto.ProductResponse;
import com.yildirimog.ecommercestaj.product.dto.ProductUpdateRequest;
import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.product.mapper.ProductMapper;
import com.yildirimog.ecommercestaj.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest dto) {
        Product product = productMapper.toEntity(dto);

        // Kategori set et (tek kategori)
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

        product.setCategories(Set.of(category));

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }
    public Page<ProductResponse> getAllProducts(Pageable pageable){

        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toResponse);
    }
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return productMapper.toResponse(product);
    }
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
        Set<Product> products = category.getProducts();
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı"));

        productMapper.updateEntityFromDto(dto, product);
        Category category = categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı"));

        product.setCategories(Set.of(category));

        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        productRepository.delete(product);
    }

}
