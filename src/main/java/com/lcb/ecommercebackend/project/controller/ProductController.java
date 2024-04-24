package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.ProductService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CommonUtil commonUtil;
    @GetMapping({"getAllProducts", "getAllProducts/"})
    public ResponseEntity<ResponseWrapper<?>> getAllProducts(){
        ResponseWrapper<?> responseWrapper = productService.getAllProducts();
        if(responseWrapper.getResult().getStatusCode()==HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @GetMapping({"getProductById/{productId}", "getProductById/", "getProductById"})
    public ResponseEntity<ResponseWrapper<?>> getProductById(@PathVariable(required = false) String productId){
        ResponseWrapper<?> responseWrapper = productService.findByProductId(productId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @PostMapping({"addProduct", "addProduct/"})
    public ResponseEntity<ResponseWrapper<?>> addNewProduct(@Valid @RequestBody ProductEntity productRequest, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = productService.addNewProduct(productRequest, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @PutMapping({"updateProduct", "updateProduct/"})
    public ResponseEntity<ResponseWrapper<?>> updateExistingProduct(@Valid @RequestBody ProductEntity productRequest, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = productService.updateExistingProduct(productRequest, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @DeleteMapping({"deleteProduct/{productId}", "deleteProduct/", "deleteProduct"})
    public ResponseEntity<ResponseWrapper<?>> deleteExistingProduct(@PathVariable(required = false) String productId){
        ResponseWrapper<?> responseWrapper = productService.deleteExistingProduct(productId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
