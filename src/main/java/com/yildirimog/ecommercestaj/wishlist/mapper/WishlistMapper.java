package com.yildirimog.ecommercestaj.wishlist.mapper;

import com.yildirimog.ecommercestaj.wishlist.dto.WishlistItemResponse;
import com.yildirimog.ecommercestaj.wishlist.entity.WishListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.imageUrl", target = "imageUrl")
    WishlistItemResponse toResponse(WishListItem item);
}
