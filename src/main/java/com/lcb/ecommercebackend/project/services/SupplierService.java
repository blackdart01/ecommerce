package com.lcb.ecommercebackend.project.services;

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
import com.lcb.ecommercebackend.project.services.transactionalService.SupplierTransaction;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private SupplierTransaction supplierTransaction;

    public ResponseWrapper<List<SupplierEntity>> getAllSuppliers() {
        List<SupplierEntity> supplierEntityList = supplierRepository.findAll();
        if (ObjectUtils.isEmpty(supplierEntityList) || supplierEntityList.isEmpty())
            return commonUtil.wrapToWrapperClass(supplierEntityList, new ResultResponse(HttpStatus.CONFLICT, StatusCodeEnum.RECORD_NOT_FOUND.getMessgage()));
        return commonUtil.wrapToWrapperClass(supplierEntityList, serviceUtil.getAllCheck(supplierEntityList));
    }

    public ResponseWrapper<?> findBySupplierId(String supplierId) {
        try {
            if (ObjectUtils.isEmpty(supplierId) || supplierId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {

                SupplierEntity entityById = supplierRepository.findBySupplierId(supplierId);
                if (ObjectUtils.isEmpty(entityById))
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                return commonUtil.wrapToWrapperClass(entityById, new ResultResponse(HttpStatus.OK));
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> addNewSupplier(SupplierEntity supplierEntity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                ResponseWrapper<List<SupplierEntity>> entityList = getAllSuppliers();
                boolean isSupplierIdNotMatched = entityList.getData().stream().filter(entity -> supplierEntity.getUserName().equalsIgnoreCase(entity.getUserName())).findFirst().isEmpty();
                if (!isSupplierIdNotMatched)
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE, null, null);
                return supplierTransaction.createNewSupplier(supplierEntity);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> updateExistingSupplier(SupplierEntity supplierEntity, String errors) {
        try {
            if (ObjectUtils.isEmpty(supplierEntity.getSupplierId()) || supplierEntity.getSupplierId().equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            }
            List<SupplierEntity> isIdNotMatched = getAllSuppliers().getData().stream().filter(entity -> supplierEntity.getSupplierId().equalsIgnoreCase(entity.getSupplierId())).toList();
            if (isIdNotMatched.isEmpty())
                return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
            else if (isIdNotMatched.size() > 1)
                return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
            else if (!supplierEntity.getUserName().equalsIgnoreCase(isIdNotMatched.get(0).getUserName()))
                return serviceUtil.buildConflictData(StatusCodeEnum.USERNAME_UNMATCHED, null, null);
            return supplierTransaction.updateSupplier(supplierEntity);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> deleteExistingSupplier(String supplierId) {
        try {
            if (ObjectUtils.isEmpty(supplierId) || supplierId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                SupplierEntity entityById = supplierRepository.findBySupplierId(supplierId);
                if (ObjectUtils.isEmpty(entityById)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    return supplierTransaction.deleteSupplier(entityById);
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> disActivateSupplier(String supplierId, boolean status) {
        try {
            if (ObjectUtils.isEmpty(supplierId) || supplierId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                SupplierEntity entityById = supplierRepository.findBySupplierId(supplierId);
                if (ObjectUtils.isEmpty(entityById))
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                return supplierTransaction.disActivateSupplier(entityById, status);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> disActivateProducts(ProductSupplierActivity productSupplierActivity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors))
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            return supplierTransaction.disActivateProducts(productSupplierActivity);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
