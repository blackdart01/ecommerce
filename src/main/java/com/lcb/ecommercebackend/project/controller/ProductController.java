package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductRequest;
import com.lcb.ecommercebackend.project.model.requests.WrapperRequest;
import com.lcb.ecommercebackend.project.model.responses.ProductResponse;
import com.lcb.ecommercebackend.project.services.ProductService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ecom/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CommonUtil commonUtil;
    @GetMapping("getAllProducts")
    public ResponseEntity<WrapperRequest> getAllProducts(){
        WrapperRequest wrapperRequest = productService.getAllProducts();
        if(wrapperRequest.getResult().getStatusCode()==HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(wrapperRequest);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(null);
    }

    @GetMapping("getProductById/{productId}")
    public ResponseEntity<WrapperRequest> getProductById(@PathVariable("productId") Long productId){
        WrapperRequest wrapperRequest = productService.findByProductId(productId);
        return ResponseEntity.status(wrapperRequest.result.getStatusCode()).body(wrapperRequest);
    }

    @PostMapping("addProduct")
    public ResponseEntity<WrapperRequest> addNewProduct(@RequestBody ProductRequest productRequest){
        WrapperRequest wrapperRequest = productService.addNewProduct(productRequest);
        return ResponseEntity.status(wrapperRequest.getResult().getStatusCode()).body(wrapperRequest);
    }

    @PutMapping("updateProduct")
    public ResponseEntity<WrapperRequest> updateExistingProduct(@RequestBody ProductRequest productRequest){
        WrapperRequest wrapperRequest = productService.updateExistingProduct(productRequest);
        return ResponseEntity.status(wrapperRequest.getResult().getStatusCode()).body(wrapperRequest);
    }

    @DeleteMapping("deleteProduct/{productId}")
    public ResponseEntity<WrapperRequest> deleteExistingProduct(@PathVariable("productId") Long productId){
        WrapperRequest wrapperRequest = productService.deleteExistingProduct(productId);
        return ResponseEntity.status(wrapperRequest.getResult().getStatusCode()).body(wrapperRequest);
    }
}
