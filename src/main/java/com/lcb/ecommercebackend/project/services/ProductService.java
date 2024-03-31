package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductRequest;
import com.lcb.ecommercebackend.project.model.requests.ResultRequest;
import com.lcb.ecommercebackend.project.model.requests.WrapperRequest;
import com.lcb.ecommercebackend.project.model.responses.ProductResponse;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommonUtil commonUtil;

    public WrapperRequest<List<ProductRequest>> getAllProducts() {
        List<ProductRequest> productRequestList = productRepository.findAll();
        ResultRequest resultRequest = new ResultRequest();
        resultRequest.setStatusCode((ObjectUtils.isEmpty(productRequestList)?HttpStatus.NO_CONTENT.value():HttpStatus.OK.value()));
        resultRequest.setErrorMsg((ObjectUtils.isEmpty(productRequestList)?"NO_DATA":"SUCCESS"));
        return commonUtil.wrapToWrapperClass(productRequestList, resultRequest);
    }

    public WrapperRequest findByProductId(Long productId) {
        return setProductResponse(productRepository.findByProductId(productId), productId, null);
    }

    public WrapperRequest addNewProduct(ProductRequest productRequest) {
        List<ProductRequest> productRequestList = getAllProducts().getData();
        if (productRequestList.stream().anyMatch(some -> some.getProductId().equals(productRequest.getProductId())))
            return setProductResponse(productRequest, productRequest.getProductId(), "DUPLICATE_PRODUCT_ID");
        else {
            productRepository.save(productRequest);
            return setProductResponse(productRequest, productRequest.getProductId(), null);
        }
    }

    public WrapperRequest updateExistingProduct(ProductRequest productRequest) {
        ProductRequest productRequest1 = productRepository.findByProductId(productRequest.getProductId());
        WrapperRequest wrapperRequest = setProductResponse(productRequest, productRequest.getProductId(), null);
        if (!ObjectUtils.isEmpty(productRequest1)) {
            if (wrapperRequest.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.save(setProductResponseToUpdate(productRequest1, productRequest));
        }
        return wrapperRequest;
    }
    public WrapperRequest deleteExistingProduct(Long productId) {
        ProductRequest productRequest = productRepository.findByProductId(productId);
        WrapperRequest wrapperRequest = setProductResponse(productRequest, productId, null);
        if (!ObjectUtils.isEmpty(productRequest)) {
            if (wrapperRequest.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.deleteById(productRequest.getId());
        }
        return wrapperRequest;
    }

    public WrapperRequest setProductResponse(ProductRequest productRequest, Long productId, String msg) {
        ProductResponse productResponse = new ProductResponse();
        ResultRequest resultRequest = new ResultRequest();
        if (ObjectUtils.isEmpty(productRequest)) {
            resultRequest.setErrorMsg(ObjectUtils.isEmpty(msg) ? "NOT_FOUND" : msg);
            resultRequest.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.NOT_FOUND.value() : HttpStatus.CONFLICT.value());
            productResponse.setProductId(productId);
            productResponse.setProductCost(null);
            productResponse.setProductDesc(null);
            productResponse.setProductType(null);
            productResponse.setProductName(null);
            productResponse.setCategory(null);
        } else {
            resultRequest.setErrorMsg(ObjectUtils.isEmpty(msg) ? "SUCCESS" : msg);
            resultRequest.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value());
            productResponse.setProductId(productRequest.getProductId());
            productResponse.setCategory(productRequest.getCategory());
            productResponse.setProductCost(productRequest.getProductCost());
            productResponse.setProductDesc(productRequest.getProductDesc());
            productResponse.setProductName(productRequest.getProductName());
            productResponse.setProductType(productRequest.getProductType());
        }
        return commonUtil.wrapToWrapperClass(productResponse, resultRequest);
    }

    public ProductRequest setProductResponseToUpdate(ProductRequest productRequestToUpdate, ProductRequest productRequest) {
        productRequestToUpdate.setCategory(productRequest.getCategory());
        productRequestToUpdate.setProductType(productRequest.getProductType());
        productRequestToUpdate.setProductCost(productRequest.getProductCost());
        productRequestToUpdate.setProductDesc(productRequest.getProductDesc());
        productRequestToUpdate.setProductName(productRequest.getProductName());
        return productRequestToUpdate;
    }
}
