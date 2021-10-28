package com.bog.demo.model.product;

import com.bog.demo.domain.file.File;
import lombok.Data;

@Data
public class ProductDto {

    private Integer id;
    private Integer fileId;
    private File file;
    private String name;
    private Double price;
    private Double quantity;
    private Integer state;
}