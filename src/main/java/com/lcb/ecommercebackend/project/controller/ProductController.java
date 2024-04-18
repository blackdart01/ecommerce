package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.ProductService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CommonUtil commonUtil;
    @GetMapping("getAllProducts")
    public ResponseEntity<ResponseWrapper> getAllProducts(){
        ResponseWrapper responseWrapper = productService.getAllProducts();
        if(responseWrapper.getResult().getStatusCode()==HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(null);
    }

    @GetMapping("getProductById/{productId}")
    public ResponseEntity<ResponseWrapper> getProductById(@PathVariable("productId") String productId){
        ResponseWrapper responseWrapper = productService.findByProductId(productId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @PostMapping("addProduct")
    public ResponseEntity<ResponseWrapper> addNewProduct(@RequestBody ProductEntity productRequest){
        ResponseWrapper responseWrapper = productService.addNewProduct(productRequest);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @PutMapping("updateProduct")
    public ResponseEntity<ResponseWrapper> updateExistingProduct(@RequestBody ProductEntity productRequest){
        ResponseWrapper responseWrapper = productService.updateExistingProduct(productRequest);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @DeleteMapping("deleteProduct/{productId}")
    public ResponseEntity<ResponseWrapper> deleteExistingProduct(@PathVariable("productId") String productId){
        ResponseWrapper responseWrapper = productService.deleteExistingProduct(productId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
