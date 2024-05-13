package com.lcb.ecommercebackend.project.services.transactionalService;

import com.lcb.ecommercebackend.project.model.Request.ProductSupplierActivity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.SupplierEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
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
public class SupplierTransaction {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private ProductRepository productRepository;
    @Transactional
    public ResponseWrapper<?> createNewSupplier(SupplierEntity supplierEntity) {
        try {
            supplierEntity.setCreatedAt(commonUtil.getCurrentDate());
            supplierEntity.setSupplierId(commonUtil.generateUniqueId("SUP"));
            SupplierEntity entitySaved = supplierRepository.save(supplierEntity);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entitySaved.getId(), entitySaved.getSupplierId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.SUPPLIER_CREATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> updateSupplier(SupplierEntity supplierEntity) {
        try {
            SupplierEntity entityById = supplierRepository.findBySupplierId(supplierEntity.getSupplierId());
            entityById.setContactEmail(supplierEntity.getContactEmail());
            entityById.setIsActive(supplierEntity.getIsActive());
            entityById.setSupplierName(supplierEntity.getSupplierName());
            entityById.setCity(supplierEntity.getCity());
            entityById.setCountry(supplierEntity.getCountry());
            entityById.setAddressLine1(supplierEntity.getAddressLine1());
            entityById.setAddressLine2(supplierEntity.getAddressLine2());
            entityById.setPostalCode(supplierEntity.getPostalCode());
            entityById.setState(supplierEntity.getState());
            entityById.setModifiedAt(ZonedDateTime.now());
            entityById.setReviewsId(supplierEntity.getReviewsId());
            entityById.setContactPhone(supplierEntity.getContactPhone());
            if((ObjectUtils.isEmpty(supplierEntity.getProducts()) && !ObjectUtils.isEmpty(entityById.getProducts()))||(!ObjectUtils.isEmpty(supplierEntity.getProducts()) && ObjectUtils.isEmpty(entityById.getProducts()))){
                return serviceUtil.buildConflictData(StatusCodeEnum.NON_UPDATION, null, null);
            }
            for(String productId: supplierEntity.getProducts()){
                if(!entityById.getProducts().contains(productId))
                    return serviceUtil.buildConflictData(StatusCodeEnum.NON_UPDATION, null, null);
            }
            SupplierEntity entitySaved = supplierRepository.save(entityById);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entitySaved.getId(), entitySaved.getSupplierId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.SUPPLIER_CREATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> deleteSupplier(SupplierEntity entityById) {
        try {
            supplierRepository.deleteById(entityById.getId());
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entityById.getId(), entityById.getSupplierId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.SUPPLIER_DELETION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> disActivateSupplier(SupplierEntity entityById, Boolean status) {
        try {
            entityById.setIsActive(status);
            SupplierEntity entity = supplierRepository.save(entityById);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entity.getId(), entity.getSupplierId(), (status?StatusCodeEnum.SUPPLIER_ACTIVATED:StatusCodeEnum.SUPPLIER_INACTIVATED).getMessgage(), (status?StatusCodeEnum.SUPPLIER_ACTIVATED:StatusCodeEnum.SUPPLIER_INACTIVATED)), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> disActivateProducts(ProductSupplierActivity productSupplierActivity) {
        try {
            if(ObjectUtils.isEmpty(productSupplierActivity.getSupplierId()) || productSupplierActivity.getSupplierId().equalsIgnoreCase("null"))
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            SupplierEntity supplierEntity = supplierRepository.findBySupplierId(productSupplierActivity.getSupplierId());
            if (ObjectUtils.isEmpty(supplierEntity)) {
                return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
            }
            if(ObjectUtils.isEmpty(supplierEntity.getProducts()))
                return serviceUtil.buildConflictData(StatusCodeEnum.NO_DATA, null, null);
            for(ProductSupplierActivity.Products productId : productSupplierActivity.getProducts()){
                if(!supplierEntity.getProducts().contains(productId.getProductId()))
                    return serviceUtil.buildConflictData(StatusCodeEnum.UNMATCHED, null, null);
            }
            for(ProductSupplierActivity.Products product : productSupplierActivity.getProducts()){
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