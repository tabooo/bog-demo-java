package com.bog.demo.controller;

import com.bog.demo.model.product.ProductDto;
import com.bog.demo.model.product.ProductSearchRequestDto;
import com.bog.demo.model.product.ProductSearchResponseDto;
import com.bog.demo.service.product.ProductService;
import com.bog.demo.util.Descriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("userManagementController")
@RequestMapping("rest/api/product")
public class ProductController {
    private ProductService productService;

    @GetMapping("search")
    public ProductSearchResponseDto searchProducts(ProductSearchRequestDto debtRequestDto) {
        return productService.searchProducts(debtRequestDto);
    }

    @PostMapping("save-product")
    public ResponseEntity<Descriptor> saveProduct(@Valid @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.saveProduct(productDto), HttpStatus.OK);
    }

    @PutMapping("update-product")
    public ResponseEntity<Descriptor> updateUser(@Valid @RequestBody ProductDto productDto) {

        return new ResponseEntity<>(productService.updateProduct(productDto), HttpStatus.OK);
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}