package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.ProductCategoryService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/product-category")
public class ProductCategoryController {
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    ServiceUtil serviceUtil;

//    @GetMapping({"getAllCategories", "getAllCategories/"})
    @GetMapping({"getAllCategories"})
    @CrossOrigin
    public ResponseEntity<ResponseWrapper<List<ProductCategoryEntity>>> getAllCategories(){
        ResponseWrapper<List<ProductCategoryEntity>> responseWrapper = productCategoryService.getAllCategories();
        if(responseWrapper.getResult().getStatusCode()== HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

//    @GetMapping({"getCategoryByCategoryId/{categoryId}", "getCategoryByCategoryId/", "getCategoryByCategoryId"})
    @GetMapping({"getCategoryByCategoryId/{categoryId}"})
    public ResponseEntity<ResponseWrapper<?>> getCategoryByCategoryId(@PathVariable(required = false) String categoryId) {
        ResponseWrapper<?> responseWrapper = productCategoryService.findByCategoryId(categoryId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

//    @PostMapping({"createCategory", "createCategory/"})
    @PostMapping({"createCategory"})
    public ResponseEntity<ResponseWrapper<?>> createNewDiscount(@Valid @RequestBody ProductCategoryEntity ProductCategoryEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = productCategoryService.createNewCategory(ProductCategoryEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @PutMapping({"updateCategory", "updateCategory/"})
    @PutMapping({"updateCategory"})
    public ResponseEntity<ResponseWrapper<?>> updateExistingCategory(@Valid @RequestBody ProductCategoryEntity ProductCategoryEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = productCategoryService.updateExistingCategory(ProductCategoryEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @DeleteMapping({"deleteCategory/{categoryId}", "deleteCategory/", "deleteCategory"})
    @DeleteMapping({"deleteCategory/{categoryId}"})
    public ResponseEntity<ResponseWrapper<?>> deleteExistingDiscount(@PathVariable(required = false) String categoryId){
        ResponseWrapper<?> responseWrapper = productCategoryService.deleteExistingCategory(categoryId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
