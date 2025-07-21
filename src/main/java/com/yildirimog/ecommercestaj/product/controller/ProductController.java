package com.yildirimog.ecommercestaj.product.controller;

import com.yildirimog.ecommercestaj.product.dto.PagedResponse;
import com.yildirimog.ecommercestaj.product.dto.ProductCreateRequest;
import com.yildirimog.ecommercestaj.product.dto.ProductResponse;
import com.yildirimog.ecommercestaj.product.dto.ProductUpdateRequest;
import com.yildirimog.ecommercestaj.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request){
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /*
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> getAllProductsPagined
            (@RequestParam(defaultValue = "0")int page,
             @RequestParam(defaultValue = "10") int size
            ){
        PageRequest pageRequest =PageRequest.of(page,size);
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        PagedResponse<ProductResponse> response = new PagedResponse<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        try {
            productService.updateProduct(id, request);
            return ResponseEntity.noContent().build(); // Return 204 No Content on success
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 Internal Server Error on failure
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
