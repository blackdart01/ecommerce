package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
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
    @Autowired
    private ServiceUtil serviceUtil;

    public ResponseWrapper<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> productEntityList = productRepository.findAll();
        if (ObjectUtils.isEmpty(productEntityList) || productEntityList.isEmpty())
            return commonUtil.wrapToWrapperClass(productEntityList, new ResultResponse(HttpStatus.CONFLICT, StatusCodeEnum.RECORD_NOT_FOUND.getMessgage()));
        return commonUtil.wrapToWrapperClass(productEntityList, serviceUtil.getAllCheck(productEntityList));
    }

    public ResponseWrapper<?> findByProductId(String productId) {
        try {
            if (ObjectUtils.isEmpty(productId) || productId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {

                ProductEntity productEntityByProductId = productRepository.findByProductId(productId);
                if (ObjectUtils.isEmpty(productEntityByProductId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    return commonUtil.wrapToWrapperClass(productEntityByProductId, new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> addNewProduct(ProductEntity productEntity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                ResponseWrapper<List<ProductEntity>> allProductsWrappper = getAllProducts();
                boolean isProductIdNotMatched = allProductsWrappper.getData().stream().filter(productEntity1 -> productEntity.getSku().equalsIgnoreCase(productEntity1.getSku())).findFirst().isEmpty();
                if (ObjectUtils.isEmpty(productEntity.getSku()) || productEntity.getSku().equalsIgnoreCase("null")) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
                } else if (!isProductIdNotMatched) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_SKU, null, null);
                } else {
                    if (Double.parseDouble(productEntity.getPrice()) < 0.0)
                        return serviceUtil.buildConflictData(StatusCodeEnum.NEGATIVE_PRICE, null, null);
                    else if (Double.parseDouble(productEntity.getUnitsOnStock()) < 0.0 || Double.parseDouble(productEntity.getUnitsOnOrder()) < 0.0)
                        return serviceUtil.buildConflictData(StatusCodeEnum.NEGATIVE_PRODUCT_QUANITY, null, null);
                    else if (productEntity.getUnitsOnStock().contains(".") || productEntity.getUnitsOnOrder().contains("."))
                        return serviceUtil.buildConflictData(StatusCodeEnum.NON_FLOAT_VALUE, null, null);
                    productEntity.setCreatedAt(commonUtil.getCurrentDate());
                    productEntity.setProductId(commonUtil.generateUniqueId("PRO"));
                    ProductEntity productEntitySaved = productRepository.save(productEntity);
                    return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(productEntitySaved.getId(), productEntitySaved.getProductId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.PRODUCT_CREATION_API), new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> updateExistingProduct(ProductEntity productEntity, String errors) {
        try {
            if (ObjectUtils.isEmpty(productEntity.getProductId()) || productEntity.getProductId().equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                List<ProductEntity> isIdNotMatched = getAllProducts().getData().stream().filter(productEntity1 -> productEntity.getProductId().equalsIgnoreCase(productEntity1.getProductId())).toList();
                ProductEntity isSkuNotMatched = productRepository.findBySku(productEntity.getSku());//getAllProducts().getData().stream().filter(productEntity1 -> productEntity.getSku().equalsIgnoreCase(productEntity1.getSku())).findFirst().isEmpty();
                if (isIdNotMatched.isEmpty())
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                else if (isIdNotMatched.size() > 1)
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
                else if (!ObjectUtils.isEmpty(isSkuNotMatched) && !isSkuNotMatched.getProductId().equalsIgnoreCase(productEntity.getProductId()))
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_SKU, null, null);
                else {
                    ProductEntity productEntityByProductId = productRepository.findByProductId(productEntity.getProductId());
                    if (Double.parseDouble(productEntity.getPrice()) < 0.0)
                        return serviceUtil.buildConflictData(StatusCodeEnum.NEGATIVE_PRICE, null, null);
                    else if (Double.parseDouble(productEntity.getUnitsOnStock()) < 0.0 || Double.parseDouble(productEntity.getUnitsOnOrder()) < 0.0)
                        return serviceUtil.buildConflictData(StatusCodeEnum.NEGATIVE_PRODUCT_QUANITY, null, null);
                    else if (productEntity.getUnitsOnStock().contains(".") || productEntity.getUnitsOnOrder().contains("."))
                        return serviceUtil.buildConflictData(StatusCodeEnum.NON_FLOAT_VALUE, null, null);
                    productEntityByProductId.setPrice(productEntity.getPrice());
                    productEntityByProductId.setIsActive(productEntity.getIsActive());
                    productEntityByProductId.setModifiedAt(commonUtil.getCurrentDate());
                    productEntityByProductId.setProductName(productEntity.getProductName());
                    productEntityByProductId.setProductDesc(productEntity.getProductDesc());
                    productEntityByProductId.setSku(productEntity.getSku());
                    productEntityByProductId.setSupplierId(productEntity.getSupplierId());
                    productEntityByProductId.setReviewsId(productEntity.getReviewsId());
                    productEntityByProductId.setUnitsOnOrder(productEntity.getUnitsOnOrder());
                    productEntityByProductId.setUnitsOnStock(productEntity.getUnitsOnStock());
                    productEntityByProductId.setDiscountId(productEntity.getDiscountId());
                    ProductEntity productEntitySaved = productRepository.save(productEntityByProductId);
                    return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(productEntitySaved.getId(), productEntitySaved.getProductId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.PRODUCT_UPDATION_API), new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> deleteExistingProduct(String productId) {
        try {
            if (ObjectUtils.isEmpty(productId) || productId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                ProductEntity productEntityByProductId = productRepository.findByProductId(productId);
                if (ObjectUtils.isEmpty(productEntityByProductId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    productRepository.deleteById(productEntityByProductId.getId());
                    return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(productEntityByProductId.getId(), productEntityByProductId.getProductId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.PRODUCT_DELETION_API), new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

//    public ResponseWrapper setProductResponse(ProductEntity productRequest, String productId, String msg) {
//        ProductResponse productResponse = new ProductResponse();
//        ResultResponse resultResponse = new ResultResponse();
//        if (ObjectUtils.isEmpty(productRequest)) {
//            resultResponse.setMessage(ObjectUtils.isEmpty(msg) ? "NOT_FOUND" : msg);
//            resultResponse.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.NOT_FOUND.value() : HttpStatus.CONFLICT.value());
//            productResponse.setProductId(productId);
//            productResponse.setProductDesc(null);
//            productResponse.setProductName(null);
//            productResponse.setSku(null);
//            productResponse.setCreatedAt(null);
//            productResponse.setModifiedAt(null);
//            productResponse.setDeletedAt(null);
//            productResponse.setCategoryId(null);
//            productResponse.setPrice(null);
//        } else {
//            resultResponse.setMessage(ObjectUtils.isEmpty(msg) ? "SUCCESS" : msg);
//            resultResponse.setStatusCode(ObjectUtils.isEmpty(msg) ? HttpStatus.OK.value() : HttpStatus.CONFLICT.value());
//            productResponse.setProductId(commonUtil.generateUniqueProductId());
//            productResponse.setProductDesc(productRequest.getProductDesc());
//            productResponse.setProductName(productRequest.getProductName());
//            productResponse.setSku(productRequest.getSku());
//            productResponse.setCategoryId(productRequest.getCategoryId());
//            productResponse.setPrice(productRequest.getPrice());
//            productResponse.setCreatedAt(ObjectUtils.isEmpty(productRequest.getCreatedAt())?commonUtil.getCurrentDate():productRequest.getCreatedAt());
//            productResponse.setModifiedAt(ObjectUtils.isEmpty(productRequest.getModifiedAt())?null:productRequest.getModifiedAt());
//        }
//        return commonUtil.wrapToWrapperClass(productResponse, resultResponse);
//    }
//
//    public ProductEntity setProductResponseToUpdate(ProductEntity productRequestToUpdate, ProductEntity productRequest) {
//        productRequestToUpdate.setCategoryId(productRequest.getCategoryId());
//        productRequestToUpdate.setPrice(productRequest.getPrice());
//        productRequestToUpdate.setProductDesc(productRequest.getProductDesc());
//        productRequestToUpdate.setProductName(productRequest.getProductName());
//        productRequestToUpdate.setSku(productRequest.getSku());
//        productRequestToUpdate.setModifiedAt(ObjectUtils.isEmpty(productRequest.getModifiedAt())?commonUtil.getCurrentDate():productRequest.getModifiedAt());
//        return productRequestToUpdate;
//    }
}
