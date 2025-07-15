package com.yildirimog.ecommercestaj.product.entity;


import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.common.BaseEntity;
import com.yildirimog.ecommercestaj.wishlist.entity.WishListItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {

    private String name;
    private String description;
    private int stockQuantity;
    private BigDecimal price;
    private String imageUrl;
    //relations
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    @OneToMany(mappedBy = "product")
    private List<WishListItem> wishlistedBy = new ArrayList<>();

    @Version
    @Setter(AccessLevel.NONE)
    private Long version;

}
