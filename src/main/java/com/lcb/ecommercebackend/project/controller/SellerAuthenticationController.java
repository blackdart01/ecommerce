package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SellerAuthenticationEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.ProductService;
import com.lcb.ecommercebackend.project.services.SellerAuthenticationService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/seller")
public class SellerAuthenticationController {

    @Autowired
    private SellerAuthenticationService sellerAuthenticationService;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping({"getSellerByParameter/{parameter}", "getSellerByParameter/", "getSellerByParameter"})
    public ResponseEntity<ResponseWrapper<?>> getSellerByParameter(@PathVariable(required = false) String parameter){
        ResponseWrapper<?> responseWrapper = sellerAuthenticationService.findByParameter(parameter);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

    @PostMapping({"addNewSeller", "addNewSeller/"})
    public ResponseEntity<ResponseWrapper<?>> addNewSeller(@Valid @RequestBody SellerAuthenticationEntity sellerAuthenticationEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = sellerAuthenticationService.createNewSeller(sellerAuthenticationEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @PutMapping({"updateExistingSeller", "updateExistingSeller/"})
    public ResponseEntity<ResponseWrapper<?>> updateExistingSeller(@Valid @RequestBody SellerAuthenticationEntity sellerAuthenticationEntity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = sellerAuthenticationService.updateExistingSupplier(sellerAuthenticationEntity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

    @DeleteMapping({"deleteExistingSeller/{supplierId}", "deleteExistingSeller/", "deleteExistingSeller"})
    public ResponseEntity<ResponseWrapper<?>> deleteExistingSeller(@PathVariable(required = false) String supplierId){
        ResponseWrapper<?> responseWrapper = sellerAuthenticationService.deleteExistingSeller(supplierId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
