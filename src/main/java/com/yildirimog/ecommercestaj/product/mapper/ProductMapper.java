package com.yildirimog.ecommercestaj.product.mapper;

import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.mapper.CategoryMapper;
import com.yildirimog.ecommercestaj.product.dto.ProductCreateRequest;
import com.yildirimog.ecommercestaj.product.dto.ProductResponse;
import com.yildirimog.ecommercestaj.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toEntity(ProductCreateRequest dto);

    @Mapping(target = "category", source = "categories", qualifiedByName = "mapFirstCategory")
    ProductResponse toResponse(Product product);

    @Named("mapFirstCategory")
    default Category mapFirstCategory(Set<Category> categories) {
        return categories.stream().findFirst().orElse(null);
    }
}
