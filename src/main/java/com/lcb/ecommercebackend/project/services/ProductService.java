package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductRequest;
import com.lcb.ecommercebackend.project.model.responses.ProductResponse;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    public List<ProductRequest> getAllProducts(){
        return productRepository.findAll();
    }
    public ProductResponse findByProductId(Integer productId){
        return setProductResponse(productRepository.findByProductId(productId), productId);
    }

    public ProductResponse setProductResponse(ProductRequest productRequest, Integer productId){
        ProductResponse productResponse = new ProductResponse();
        if(ObjectUtils.isEmpty(productRequest)) {
            productResponse.setErrorMsg("NOT_FOUND");
            productResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            productResponse.setProductId(productId);
            productResponse.setProductCost(null);
            productResponse.setProductDesc(null);
            productResponse.setProductType(null);
            productResponse.setProductName(null);
            productResponse.setCategory(null);
        } else {
            productResponse.setErrorMsg("SUCCESS");
            productResponse.setStatusCode(HttpStatus.OK.value());
            productResponse.setProductId(productRequest.getProductId());
            productResponse.setCategory(productRequest.getCategory());
            productResponse.setProductCost(productRequest.getProductCost());
            productResponse.setProductDesc(productRequest.getProductDesc());
            productResponse.setProductName(productRequest.getProductName());
            productResponse.setProductType(productRequest.getProductType());
        }
        return productResponse;
    }
}
