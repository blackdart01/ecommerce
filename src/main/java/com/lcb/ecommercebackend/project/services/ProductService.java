package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
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

    public ResponseWrapper<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> productRequestList = productRepository.findAll();
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setStatusCode((ObjectUtils.isEmpty(productRequestList)?HttpStatus.NO_CONTENT.value():HttpStatus.OK.value()));
        resultResponse.setMessage((ObjectUtils.isEmpty(productRequestList)?"NO_DATA":"SUCCESS"));
        return commonUtil.wrapToWrapperClass(productRequestList, resultResponse);
    }

    public ResponseWrapper findByProductId(String productId) {
        return setProductResponse(productRepository.findByProductId(productId), productId, null);
    }

    public ResponseWrapper addNewProduct(ProductEntity productRequest) {
        List<ProductEntity> productRequestList = getAllProducts().getData();
        if (productRequestList.stream().anyMatch(some -> some.getProductId().equals(productRequest.getProductId())))
            return setProductResponse(productRequest, productRequest.getProductId(), "DUPLICATE_PRODUCT_ID");
        else {
            productRequest.setCreatedAt(ObjectUtils.isEmpty(productRequest.getCreatedAt())?commonUtil.getCurrentDate():productRequest.getCreatedAt());
            productRequest.setModifiedAt(ObjectUtils.isEmpty(productRequest.getModifiedAt())?null:productRequest.getModifiedAt());
            productRepository.save(productRequest);
            return setProductResponse(productRequest, productRequest.getProductId(), null);
        }
    }

    public ResponseWrapper updateExistingProduct(ProductEntity productRequest) {
        ProductEntity productRequest1 = productRepository.findByProductId(productRequest.getProductId());
        ResponseWrapper responseWrapper = setProductResponse(productRequest, productRequest.getProductId(), null);
        if (!ObjectUtils.isEmpty(productRequest1)) {
            if (responseWrapper.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.save(setProductResponseToUpdate(productRequest1, productRequest));
        }
        return responseWrapper;
    }
    public ResponseWrapper deleteExistingProduct(String productId) {
        ProductEntity productRequest = productRepository.findByProductId(productId);
        ResponseWrapper responseWrapper = setProductResponse(productRequest, productId, null);
        if (!ObjectUtils.isEmpty(productRequest)) {
            if (responseWrapper.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.deleteById(productRequest.getId());
        }
        return responseWrapper;
    }

    public ResponseWrapper setProductResponse(ProductEntity productRequest, String productId, String msg) {
        ProductResponse productResponse = new ProductResponse();
        ResultResponse resultResponse = new ResultResponse();
        if (ObjectUtils.isEmpty(productRequest)) {
            resultResponse.setMessage(ObjectUtils.isEmpty(msg) ? "NOT_FOUND" : msg);
            resultResponse.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.NOT_FOUND.value() : HttpStatus.CONFLICT.value());
            productResponse.setProductId(productId);
            productResponse.setProductDesc(null);
            productResponse.setProductName(null);
            productResponse.setSku(null);
            productResponse.setCreatedAt(null);
            productResponse.setModifiedAt(null);
            productResponse.setDeletedAt(null);
            productResponse.setCategoryId(null);
            productResponse.setPrice(null);
        } else {
            resultResponse.setMessage(ObjectUtils.isEmpty(msg) ? "SUCCESS" : msg);
            resultResponse.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value());
            productResponse.setProductId(commonUtil.generateUniqueProductId());
            productResponse.setProductDesc(productRequest.getProductDesc());
            productResponse.setProductName(productRequest.getProductName());
            productResponse.setSku(productRequest.getSku());
            productResponse.setCategoryId(productRequest.getCategoryId());
            productResponse.setPrice(productRequest.getPrice());
            productResponse.setCreatedAt(ObjectUtils.isEmpty(productRequest.getCreatedAt())?commonUtil.getCurrentDate():productRequest.getCreatedAt());
            productResponse.setModifiedAt(ObjectUtils.isEmpty(productRequest.getModifiedAt())?null:productRequest.getModifiedAt());
        }
        return commonUtil.wrapToWrapperClass(productResponse, resultResponse);
    }

    public ProductEntity setProductResponseToUpdate(ProductEntity productRequestToUpdate, ProductEntity productRequest) {
        productRequestToUpdate.setCategoryId(productRequest.getCategoryId());
        productRequestToUpdate.setPrice(productRequest.getPrice());
        productRequestToUpdate.setProductDesc(productRequest.getProductDesc());
        productRequestToUpdate.setProductName(productRequest.getProductName());
        productRequestToUpdate.setSku(productRequest.getSku());
        productRequestToUpdate.setModifiedAt(ObjectUtils.isEmpty(productRequest.getModifiedAt())?commonUtil.getCurrentDate():productRequest.getModifiedAt());
        return productRequestToUpdate;
    }
}
