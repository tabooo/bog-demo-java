package com.bog.demo.service.product;

import com.bog.demo.model.product.ProductDto;
import com.bog.demo.model.product.ProductSearchRequestDto;
import com.bog.demo.model.product.ProductSearchResponseDto;
import com.bog.demo.util.Descriptor;

public interface ProductService {
    ProductSearchResponseDto searchProducts(ProductSearchRequestDto debtRequestDto);

    Descriptor saveProduct(ProductDto productDto);

    Descriptor updateProduct(ProductDto productDto);
}