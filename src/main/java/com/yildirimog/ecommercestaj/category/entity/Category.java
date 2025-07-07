package com.yildirimog.ecommercestaj.category.entity;

import com.yildirimog.ecommercestaj.common.BaseEntity;
import com.yildirimog.ecommercestaj.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity {
    private String name;
    private String description;
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
}
