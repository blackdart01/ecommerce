package com.lcb.ecommercebackend.project.services.transactionalService;

import com.lcb.ecommercebackend.project.model.dbSchema.SellerAuthenticationEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
import com.lcb.ecommercebackend.project.repositories.SellerAuthenticationRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class SellerAuthenticationTransaction {
    @Autowired
    private SellerAuthenticationRepository sellerAuthenticationRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Transactional
    public ResponseWrapper<?> createNewSeller(SellerAuthenticationEntity sellerAuthenticationEntity) {
        try {
            sellerAuthenticationEntity.setCreatedAt(commonUtil.getCurrentDate());
            sellerAuthenticationEntity.setSupplierId(commonUtil.generateUniqueId("SUP"));
            SellerAuthenticationEntity entitySaved = sellerAuthenticationRepository.save(sellerAuthenticationEntity);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entitySaved.getId(), entitySaved.getSupplierId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.SUPPLIER_CREATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> updateSeller(SellerAuthenticationEntity sellerAuthenticationEntity) {
        try {
            SellerAuthenticationEntity entityById = sellerAuthenticationRepository.findBySupplierId(sellerAuthenticationEntity.getSupplierId());
            entityById.setContactEmail(sellerAuthenticationEntity.getContactEmail());
            entityById.setIsActive(sellerAuthenticationEntity.getIsActive());
            entityById.setSupplierName(sellerAuthenticationEntity.getSupplierName());
            entityById.setModifiedAt(ZonedDateTime.now());
            entityById.setContactPhone(sellerAuthenticationEntity.getContactPhone());
            SellerAuthenticationEntity entitySaved = sellerAuthenticationRepository.save(entityById);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entitySaved.getId(), entitySaved.getSupplierId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.SUPPLIER_CREATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> deleteSeller(SellerAuthenticationEntity entityById) {
        try {
            sellerAuthenticationRepository.deleteById(entityById.getId());
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entityById.getId(), entityById.getSupplierId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.SUPPLIER_DELETION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> disActivateSellerId(SellerAuthenticationEntity entityById, Boolean status) {
        try {
            entityById.setIsActive(status);
            SellerAuthenticationEntity entity = sellerAuthenticationRepository.save(entityById);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(entity.getId(), entity.getSupplierId(), (status?StatusCodeEnum.SUPPLIER_ACTIVATED:StatusCodeEnum.SUPPLIER_INACTIVATED).getMessgage(), (status?StatusCodeEnum.SUPPLIER_ACTIVATED:StatusCodeEnum.SUPPLIER_INACTIVATED)), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}