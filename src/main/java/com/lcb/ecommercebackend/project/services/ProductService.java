package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
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

    public WrapperRequest<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> productRequestList = productRepository.findAll();
        ResultRequest resultRequest = new ResultRequest();
        resultRequest.setStatusCode((ObjectUtils.isEmpty(productRequestList)?HttpStatus.NO_CONTENT.value():HttpStatus.OK.value()));
        resultRequest.setErrorMsg((ObjectUtils.isEmpty(productRequestList)?"NO_DATA":"SUCCESS"));
        return commonUtil.wrapToWrapperClass(productRequestList, resultRequest);
    }

    public WrapperRequest findByProductId(Long productId) {
        return setProductResponse(productRepository.findByProductId(productId), productId, null);
    }

    public WrapperRequest addNewProduct(ProductEntity productRequest) {
        List<ProductEntity> productRequestList = getAllProducts().getData();
        if (productRequestList.stream().anyMatch(some -> some.getProductId().equals(productRequest.getProductId())))
            return setProductResponse(productRequest, productRequest.getProductId(), "DUPLICATE_PRODUCT_ID");
        else {
            productRepository.save(productRequest);
            return setProductResponse(productRequest, productRequest.getProductId(), null);
        }
    }

    public WrapperRequest updateExistingProduct(ProductEntity productRequest) {
        ProductEntity productRequest1 = productRepository.findByProductId(productRequest.getProductId());
        WrapperRequest wrapperRequest = setProductResponse(productRequest, productRequest.getProductId(), null);
        if (!ObjectUtils.isEmpty(productRequest1)) {
            if (wrapperRequest.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.save(setProductResponseToUpdate(productRequest1, productRequest));
        }
        return wrapperRequest;
    }
    public WrapperRequest deleteExistingProduct(Long productId) {
        ProductEntity productRequest = productRepository.findByProductId(productId);
        WrapperRequest wrapperRequest = setProductResponse(productRequest, productId, null);
        if (!ObjectUtils.isEmpty(productRequest)) {
            if (wrapperRequest.getResult().getStatusCode() == HttpStatus.OK.value())
                productRepository.deleteById(productRequest.getId());
        }
        return wrapperRequest;
    }

    public WrapperRequest setProductResponse(ProductEntity productRequest, Long productId, String msg) {
        ProductResponse productResponse = new ProductResponse();
        ResultRequest resultRequest = new ResultRequest();
        if (ObjectUtils.isEmpty(productRequest)) {
            resultRequest.setErrorMsg(ObjectUtils.isEmpty(msg) ? "NOT_FOUND" : msg);
            resultRequest.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.NOT_FOUND.value() : HttpStatus.CONFLICT.value());
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
            resultRequest.setErrorMsg(ObjectUtils.isEmpty(msg) ? "SUCCESS" : msg);
            resultRequest.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value());
            productResponse.setProductId(productRequest.getProductId());
            productResponse.setProductDesc(productRequest.getProductDesc());
            productResponse.setProductName(productRequest.getProductName());
            productResponse.setSku(productRequest.getSku());
            productResponse.setCategoryId(productRequest.getCategoryId());
            productResponse.setPrice(productRequest.getPrice());
            productResponse.setCreatedAt(ObjectUtils.isEmpty(productRequest.getCreatedAt())?commonUtil.getCurrentDate():productRequest.getCreatedAt());
            productResponse.setModifiedAt(productRequest.getModifiedAt());
            productResponse.setDeletedAt(productRequest.getDeletedAt());
        }
        return commonUtil.wrapToWrapperClass(productResponse, resultRequest);
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
