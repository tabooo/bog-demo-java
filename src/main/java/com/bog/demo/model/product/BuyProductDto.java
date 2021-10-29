package com.bog.demo.model.product;

import lombok.Data;

@Data
public class BuyProductDto {

    private Integer productId;
    private Integer quantity;
    private String cardInfo;
}