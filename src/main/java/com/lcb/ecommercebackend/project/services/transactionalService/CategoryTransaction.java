package com.lcb.ecommercebackend.project.services.transactionalService;

import com.lcb.ecommercebackend.project.model.Request.ProductSupplierActivity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
import com.lcb.ecommercebackend.project.repositories.ProductCategoryRepository;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.repositories.SupplierRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;

@Service
public class CategoryTransaction {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ResponseWrapper<?> createNewCategory(ProductCategoryEntity productCategoryEntity) {
        try {
            productCategoryEntity.setCreatedAt(commonUtil.getCurrentDate());
            productCategoryEntity.setCategoryId(commonUtil.generateUniqueId("CAT"));
            ProductCategoryEntity entitySaved = productCategoryRepository.save(productCategoryEntity);
            if (!ObjectUtils.isEmpty(entitySaved) && !ObjectUtils.isEmpty(entitySaved))
                return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entitySaved.getId(), entitySaved.getCategoryId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.PRODUCT_CATEGORY_CREATION_API), new ResultResponse(HttpStatus.OK));
            else
                return serviceUtil.buildConflictData(StatusCodeEnum.DATA_NOT_SAVED, null, null);

        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> updateExistingCategory(ProductCategoryEntity productCategoryEntity) {
        try {
            ProductCategoryEntity productCategoryEntityByCategoryId = productCategoryRepository.findByCategoryId(productCategoryEntity.getCategoryId());
            productCategoryEntityByCategoryId.setCategoryId(productCategoryEntity.getCategoryId());
            productCategoryEntityByCategoryId.setIsActive(productCategoryEntity.getIsActive());
            productCategoryEntityByCategoryId.setModifiedAt(commonUtil.getCurrentDate());
            ProductCategoryEntity entityById = productCategoryRepository.save(productCategoryEntityByCategoryId);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entityById.getId(), entityById.getCategoryId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.CATEGORY_UPDATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> deleteExistingCategory(ProductCategoryEntity productCategoryByCategoryId) {
        try {
            productCategoryRepository.deleteById(productCategoryByCategoryId.getId());
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(productCategoryByCategoryId.getId(), productCategoryByCategoryId.getCategoryId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.CATEGORY_DELETION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> disActivateSupplier(SupplierEntity entityById, Boolean status) {
        try {
            entityById.setIsActive(status);
            SupplierEntity entity = supplierRepository.save(entityById);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entity.getId(), entity.getSupplierId(), (status ? StatusCodeEnum.SUPPLIER_ACTIVATED : StatusCodeEnum.SUPPLIER_INACTIVATED).getMessgage(), (status ? StatusCodeEnum.SUPPLIER_ACTIVATED : StatusCodeEnum.SUPPLIER_INACTIVATED)), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> disActivateProducts(ProductSupplierActivity productSupplierActivity) {
        try {
            if (ObjectUtils.isEmpty(productSupplierActivity.getSupplierId()) || productSupplierActivity.getSupplierId().equalsIgnoreCase("null"))
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            SupplierEntity supplierEntity = supplierRepository.findBySupplierId(productSupplierActivity.getSupplierId());
            if (ObjectUtils.isEmpty(supplierEntity)) {
                return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
            }
            if (ObjectUtils.isEmpty(supplierEntity.getProducts()))
                return serviceUtil.buildConflictData(StatusCodeEnum.NO_DATA, null, null);
            for (ProductSupplierActivity.Products productId : productSupplierActivity.getProducts()) {
                if (!supplierEntity.getProducts().contains(productId.getProductId()))
                    return serviceUtil.buildConflictData(StatusCodeEnum.UNMATCHED, null, null);
            }
            for (ProductSupplierActivity.Products product : productSupplierActivity.getProducts()) {
                ProductEntity productEntity = productRepository.findByProductId(product.getProductId());
                productEntity.setIsActive(product.getStatus());
                productRepository.save(productEntity);
            }
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(supplierEntity.getId(), supplierEntity.getSupplierId(), StatusCodeEnum.PRODUCT_BULK_STATUS_UPDATE.getMessgage(), StatusCodeEnum.PRODUCT_BULK_STATUS_UPDATE), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}