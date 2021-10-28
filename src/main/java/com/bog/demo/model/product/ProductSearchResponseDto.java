package com.bog.demo.model.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductSearchResponseDto {

    private Long count;
    private List<ProductDto> products;
    private Double amount;

    public ProductSearchResponseDto() {
    }

    public ProductSearchResponseDto(Long count, List<ProductDto> debts) {
        this.count = count;
        this.products = debts;
    }
}
