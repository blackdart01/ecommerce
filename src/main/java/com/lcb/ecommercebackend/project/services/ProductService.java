package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.DiscountRepository;
import com.lcb.ecommercebackend.project.repositories.ProductCategoryRepository;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.repositories.SupplierRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.TransactionRolledbackException;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private DiscountRepository discountRepository;

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

    @Transactional
    public ResponseWrapper<?> addNewProduct(ProductEntity productEntity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                ResponseWrapper<List<ProductEntity>> allProductsWrappper = getAllProducts();
                boolean isProductIdNotMatched = allProductsWrappper.getData().stream().filter(productEntity1 -> productEntity.getSku().equalsIgnoreCase(productEntity1.getSku())).findFirst().isEmpty();
                ProductCategoryEntity productCategory = productCategoryRepository.findByCategoryId("CATOTDEFEC");
                SupplierEntity supplierEntity = supplierRepository.findBySupplierId(productEntity.getSupplierId());
                if (ObjectUtils.isEmpty(productCategory) || ObjectUtils.isEmpty(supplierEntity)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.ID_UNMATCHED, null, null);
                } else {
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
                        if (ObjectUtils.isEmpty(productEntity.getCategoryId())) {
                            productEntity.setCategoryId("CATOTDEFEC");
                        }
                        productEntity.setCreatedAt(commonUtil.getCurrentDate());
                        productEntity.setProductId(commonUtil.generateUniqueId("PRO"));
                        List<String> productListForCategory = new ArrayList<>();
                        List<String> productListForSupplier = new ArrayList<>();
                        productListForSupplier.addAll(ObjectUtils.isEmpty(supplierEntity.getProducts()) ? new ArrayList<>() : supplierEntity.getProducts());
                        productListForCategory.addAll(ObjectUtils.isEmpty(productCategory.getProductList()) ? new ArrayList<>() : productCategory.getProductList());
                        productListForSupplier.add(productEntity.getProductId());
                        productListForCategory.add(productEntity.getProductId());
                        supplierEntity.setProducts(productListForSupplier);
                        productCategory.setProductList(productListForCategory);
                        ProductCategoryEntity productCategoryEntity = productCategoryRepository.save(productCategory);
                        SupplierEntity supplierEntity1 = supplierRepository.save(supplierEntity);
                        if (ObjectUtils.isEmpty(productCategoryEntity) || ObjectUtils.isEmpty(supplierEntity1))
                            return serviceUtil.buildConflictData(StatusCodeEnum.UNSAVED, null, null);
                        else {
                            ProductEntity productEntitySaved = productRepository.save(productEntity);
                            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(productEntitySaved.getId(), productEntitySaved.getProductId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.PRODUCT_CREATION_API), new ResultResponse(HttpStatus.OK));
                        }
                    }
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
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
                    else if (ObjectUtils.isEmpty(productCategoryRepository.findByCategoryId(productEntity.getCategoryId())))
                        return serviceUtil.buildConflictData(StatusCodeEnum.ID_UNMATCHED, null, null);
                    else if (ObjectUtils.isEmpty(supplierRepository.findBySupplierId(productEntity.getSupplierId())))
                        return serviceUtil.buildConflictData(StatusCodeEnum.ID_UNMATCHED, null, null);
                    else if(!productEntity.getDiscountId().equalsIgnoreCase(productEntityByProductId.getDiscountId())){
                        DiscountEntity discountEntity = discountRepository.findByDiscountId(productEntity.getDiscountId());
                        DiscountEntity discountEntityOriginal = discountRepository.findByDiscountId(productEntityByProductId.getDiscountId());
                        if(ObjectUtils.isEmpty(discountEntity) && !ObjectUtils.isEmpty(productEntity.getDiscountId()))
                            return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
                        else if(!ObjectUtils.isEmpty(discountEntityOriginal))
                            discountRepository.deleteById(discountEntityOriginal.getId());
                    }
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
}
