package com.bog.demo.mapper.product;

import com.bog.demo.domain.file.File;
import com.bog.demo.domain.product.Product;
import com.bog.demo.model.product.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product mapToEntity(ProductDto dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        if (dto.getFileId() != null) {
            product.setFile(new File(dto.getFileId()));
        } else {
            product.setFile(null);
        }
        product.setQuantity(dto.getQuantity());
        product.setId(dto.getId());
        product.setState(dto.getState());
        product.setUserId(dto.getUserId());

        return product;
    }


    public ProductDto mapToDto(Product product) {

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setState(product.getState());
        dto.setPrice(product.getPrice());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setFile(product.getFile());
        dto.setUserId(product.getUserId());

        return dto;
    }
}