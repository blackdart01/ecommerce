package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.DiscountService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    @Autowired
    DiscountService discountService;
    @Autowired
    CommonUtil commonUtil;

    @GetMapping("getAllDiscounts")
    public ResponseEntity<ResponseWrapper<List<DiscountEntity>>> getAllDiscounts(){
        ResponseWrapper<List<DiscountEntity>> responseWrapper = discountService.getAllDiscounts();
        if(responseWrapper.getResult().getStatusCode()== HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(null);
    }

    @GetMapping("getDiscountByDiscountId/{discountId}")
    public ResponseEntity<ResponseWrapper> getDiscountByDiscountId(@PathVariable("discountId") String discountId){
        ResponseWrapper responseWrapper = discountService.findByDiscountId(discountId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @GetMapping("getDiscountByProductId/{productId}")
    public ResponseEntity<ResponseWrapper<DiscountEntity>> getDiscountByProductId(@PathVariable("productId") String productId){
        ResponseWrapper responseWrapper = discountService.findByProductId(productId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @PostMapping("createDiscount")
    public ResponseEntity<ResponseWrapper<?>> createNewDiscount(@RequestBody DiscountEntity discountEntity){
        ResponseWrapper<?> responseWrapper = discountService.createNewDiscount(discountEntity);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @PutMapping("updateProduct")
    public ResponseEntity<ResponseWrapper<?>> updateExistingDiscount(@RequestBody DiscountEntity discountEntity){
        ResponseWrapper<?> responseWrapper = discountService.updateExistingDiscount(discountEntity);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @DeleteMapping("deleteProduct/{discountId}")
    public ResponseEntity<ResponseWrapper<?>> deleteExistingDiscount(@PathVariable("discountId") String discountId){
        ResponseWrapper<?> responseWrapper = discountService.deleteExistingDiscount(discountId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
