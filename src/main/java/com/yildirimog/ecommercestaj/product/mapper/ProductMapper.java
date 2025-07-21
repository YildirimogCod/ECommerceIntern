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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

    @Mapper(componentModel = "spring",
            uses = {CategoryMapper.class},
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
    )
    public abstract class ProductMapper {
        //CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

        @Autowired
        protected CategoryMapper categoryMapper;
        @Mapping(target = "categories", ignore = true)
        public abstract Product toEntity(ProductCreateRequest dto);
        @Mapping(target = "category", source = "categories", qualifiedByName = "mapFirstCategory")
        public abstract ProductResponse toResponse(Product product);

        @Named("mapFirstCategory")
        public CategoryResponse mapFirstCategory(Set<Category> categories) {
            return categories.stream()
                    .findFirst()
                    .map(categoryMapper::toResponse)
                    .orElse(null);
        }

        public abstract void updateEntityFromDto(ProductUpdateRequest dto, @MappingTarget Product product);
        /*
        @Mapping(target = "categories", ignore = true)
        Product toEntity(ProductCreateRequest dto);

        @Mapping(target = "category", source = "categories", qualifiedByName = "mapFirstCategory")
        ProductResponse toResponse(Product product);

        @Named("mapFirstCategory")
        default CategoryResponse mapFirstCategory(Set<Category> categories, @Context CategoryMapper categoryMapper) {
            return categories.stream()
                    .findFirst()
                    .map(categoryMapper::toResponse)
                    .orElse(null);
        }
        void updateEntityFromDto(ProductUpdateRequest dto, @MappingTarget Product product);

         */
    }
