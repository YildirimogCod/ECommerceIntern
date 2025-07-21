package com.yildirimog.ecommercestaj.product.service;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.mapper.CategoryMapper;
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
import java.util.stream.Collectors;

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
    /*
    @Transactional
    public void updateProduct(Long id, ProductUpdateRequest request) {
        int affected = productRepository.updateProductWithoutCategory(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                request.imageUrl()
        );

        if (affected == 0) {
            throw new EntityNotFoundException("Product not found.");
        }

        // Kategori elle güncellenir çünkü ManyToMany
//        Category category = categoryRepository.findById(request.categoryId())
//                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı."));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı."));

       // product.setCategories(Set.of(category)); // Set<Category> içeriğini değiştir
        productRepository.save(product); // ilişkiyi güncellemek için tekrar save
    }

     */

    @Transactional
    public void updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı"));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        Set<Category> updatedCategories = categoryRepository.findAllById(request.categoryIds())
                .stream().collect(Collectors.toSet());
        product.getCategories().clear();
        product.getCategories().addAll(updatedCategories);
        productRepository.save(product);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        productRepository.delete(product);
    }

}
