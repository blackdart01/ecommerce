package com.lcb.ecommercebackend.project.controller;

import com.lcb.ecommercebackend.project.model.Request.ProductSupplierActivity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.services.ProductService;
import com.lcb.ecommercebackend.project.services.SupplierService;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CommonUtil commonUtil;
//    @GetMapping({"v1/getAllSuppliers", "v1/getAllSuppliers/"})
    @GetMapping({"v1/getAllSuppliers"})
    public ResponseEntity<ResponseWrapper<?>> getAllSuppliers(){
        ResponseWrapper<?> responseWrapper = supplierService.getAllSuppliers();
        if(responseWrapper.getResult().getStatusCode()==HttpStatus.OK.value())
            return ResponseEntity.status(HttpStatus.OK.value()).body(responseWrapper);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

//    @GetMapping({"v1/getSupplierById/{supplierId}", "v1/getSupplierById/", "v1/getSupplierById"})
    @GetMapping({"v1/getSupplierById/{supplierId}"})
    public ResponseEntity<ResponseWrapper<?>> getSupplierBySupplierId(@PathVariable(required = false) String supplierId){
        ResponseWrapper<?> responseWrapper = supplierService.findBySupplierId(supplierId);
        return ResponseEntity.status(responseWrapper.result.getStatusCode()).body(responseWrapper);
    }

//    @PostMapping({"v1/addSupplier", "v1/addSupplier/"})
    @PostMapping({"v1/addSupplier"})
    public ResponseEntity<ResponseWrapper<?>> addNewSupplier(@Valid @RequestBody SupplierEntity entity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = supplierService.addNewSupplier(entity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @PutMapping({"v1/updateSupplier", "v1/updateSupplier/"})
    @PutMapping({"v1/updateSupplier"})
    public ResponseEntity<ResponseWrapper<?>> updateExistingSupplier(@Valid @RequestBody SupplierEntity entity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = supplierService.updateExistingSupplier(entity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @DeleteMapping({"v1/deleteSupplier/{supplierId}", "v1/deleteSupplier/", "v1/deleteSupplier"})
    @DeleteMapping({"v1/deleteSupplier/{supplierId}"})
    public ResponseEntity<ResponseWrapper<?>> deleteExistingSupplier(@PathVariable(required = false) String supplierId){
        ResponseWrapper<?> responseWrapper = supplierService.deleteExistingSupplier(supplierId);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @PatchMapping({"v1/disActivateSupplier/{supplierId}/{status}", "v1/disActivateSupplier/", "v1/disActivateSupplier"})
    @PatchMapping({"v1/disActivateSupplier/{supplierId}/{status}"})
    public ResponseEntity<ResponseWrapper<?>> disActivateSupplier(@PathVariable(required = false) String supplierId, @PathVariable(required = false) Boolean status){
        ResponseWrapper<?> responseWrapper = supplierService.disActivateSupplier(supplierId, status);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }

//    @PostMapping({"v1/disActivateSuppliersProducts", "v1/disActivateSuppliersProducts/"})
    @PostMapping({"v1/disActivateSuppliersProducts"})
    public ResponseEntity<ResponseWrapper<?>> disActivateSuppliersProducts(@Valid @RequestBody ProductSupplierActivity productSupplierActivity, BindingResult bindingResult){
        String errors = null;
        if(bindingResult.hasErrors()){
            errors = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        ResponseWrapper<?> responseWrapper = supplierService.disActivateProducts(productSupplierActivity, errors);
        return ResponseEntity.status(responseWrapper.getResult().getStatusCode()).body(responseWrapper);
    }
}
