package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.DiscountService;
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
@RequestMapping("/discount")
public class DiscountController {
    @Autowired
    DiscountService discountService;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    ServiceUtil serviceUtil;

    @GetMapping({"getAllDiscounts", "getAllDiscounts/"})
    public ResponseEntity<ResponseWrapper<List<DiscountEntity>>> getAllDiscounts(){
        ResponseWrapper<List<DiscountEntity>> responseWrapper = discountService.getAllDiscounts();
        if(responseWrapper.getResult().getStatusCode()== HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @GetMapping({"getDiscountByDiscountId/{discountId}", "getDiscountByDiscountId/", "getDiscountByDiscountId"})
    public ResponseEntity<ResponseWrapper<?>> getDiscountByDiscountId(@PathVariable(required = false) String discountId){
        ResponseWrapper<?> responseWrapper = discountService.findByDiscountId(discountId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }
    @GetMapping({"getDiscountByProductId/{productId}", "getDiscountByProductId/", "getDiscountByProductId"})
    public ResponseEntity<ResponseWrapper<?>> getDiscountByProductId(@PathVariable(required = false) String productId){
        ResponseWrapper<?> responseWrapper = discountService.findByProductId(productId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @PostMapping({"createDiscount", "createDiscount/"})
    public ResponseEntity<ResponseWrapper<?>> createNewDiscount(@Valid @RequestBody DiscountEntity discountEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            String errorList = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            errors = errorList;//.stream().collect(Collectors.joining("\n"));
        }
        ResponseWrapper<?> responseWrapper = discountService.createNewDiscount(discountEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @PutMapping({"updateDiscount", "updateDiscount/"})
    public ResponseEntity<ResponseWrapper<?>> updateExistingDiscount(@Valid @RequestBody DiscountEntity discountEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            String errorList = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            errors = errorList;//.stream().collect(Collectors.joining("\n"));
        }
        ResponseWrapper<?> responseWrapper = discountService.updateExistingDiscount(discountEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @DeleteMapping({"deleteProduct/{discountId}", "deleteProduct/", "deleteProduct"})
    public ResponseEntity<ResponseWrapper<?>> deleteExistingDiscount(@PathVariable(required = false) String discountId){
        ResponseWrapper<?> responseWrapper = discountService.deleteExistingDiscount(discountId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
