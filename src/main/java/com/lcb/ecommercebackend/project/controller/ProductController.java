package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductRequest;
import com.lcb.ecommercebackend.project.model.responses.ProductResponse;
import com.lcb.ecommercebackend.project.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ecom/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("getAllProducts")
    public ResponseEntity<List<ProductRequest>> getAllProducts(){
        List<ProductRequest> productRequest = productService.getAllProducts();
        if(ObjectUtils.isEmpty(productRequest))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(null);
        return ResponseEntity.status(HttpStatus.OK.value()).body(productRequest);
    }

    @GetMapping("getProductById/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Integer productId){
        ProductResponse productResponse = productService.findByProductId(productId);
        return ResponseEntity.status(ObjectUtils.isEmpty(productResponse.getProductName())?HttpStatus.BAD_REQUEST.value():HttpStatus.OK.value()).body(productResponse);
    }
}
