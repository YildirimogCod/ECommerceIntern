package com.yildirimog.ecommercestaj.category.mapper;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;
import com.yildirimog.ecommercestaj.category.entity.Category;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

}
