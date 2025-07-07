package com.yildirimog.ecommercestaj.product.service;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
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
import java.util.List;
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
    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
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
    public ProductResponse updateProduct(Long id, ProductCreateRequest dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        productMapper.updateEntityFromDto(dto, product);

        // Kategori set et (tek kategori)
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));

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
