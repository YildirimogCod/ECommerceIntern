package com.yildirimog.ecommercestaj.product.mapper;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import com.yildirimog.ecommercestaj.category.mapper.CategoryMapper;
import com.yildirimog.ecommercestaj.product.dto.ProductCreateRequest;
import com.yildirimog.ecommercestaj.product.dto.ProductResponse;
import com.yildirimog.ecommercestaj.product.dto.ProductUpdateRequest;
import com.yildirimog.ecommercestaj.product.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Set;

    @Mapper(componentModel = "spring",
            uses = {CategoryMapper.class},
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    public interface ProductMapper {
        CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
        @Mapping(target = "categories", ignore = true)
        Product toEntity(ProductCreateRequest dto);

        @Mapping(target = "category", source = "categories", qualifiedByName = "mapFirstCategory")
        ProductResponse toResponse(Product product);

        @Named("mapFirstCategory")
        default CategoryResponse mapFirstCategory(Set<Category> categories) {
            return categories.stream()
                    .findFirst()
                    .map(categoryMapper::toResponse)
                    .orElse(null);
        }
        void updateEntityFromDto(ProductUpdateRequest dto, @MappingTarget Product product);
    }
