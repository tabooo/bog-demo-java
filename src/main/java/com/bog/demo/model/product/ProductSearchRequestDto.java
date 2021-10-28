package com.bog.demo.model.product;

import lombok.Data;

@Data
public class ProductSearchRequestDto {

    private Integer firstResult;
    private Integer limit;
    private String name;
}
