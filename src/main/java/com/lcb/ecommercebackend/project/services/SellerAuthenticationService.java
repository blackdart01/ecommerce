package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.SellerAuthenticationEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
import com.lcb.ecommercebackend.project.repositories.SellerAuthenticationRepository;
import com.lcb.ecommercebackend.project.services.transactionalService.SellerAuthenticationTransaction;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.ObjectUtils;
import java.util.List;

@Service
public class SellerAuthenticationService {
    @Autowired
    private SellerAuthenticationRepository sellerAuthenticationRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private SellerAuthenticationTransaction sellerAuthenticationTransaction;

    public ResponseWrapper<?> findByParameter(String parameter) {
        try {
            if (ObjectUtils.isEmpty(parameter) || parameter.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                SellerAuthenticationEntity sellerAuthenticationEntity = sellerAuthenticationRepository.findBySupplierId(parameter);
                SellerAuthenticationEntity sellerAuthenticationEntity1 = sellerAuthenticationRepository.findByContactEmail(parameter);
                SellerAuthenticationEntity sellerAuthenticationEntity2 = sellerAuthenticationRepository.findByContactPhone(parameter);
                SellerAuthenticationEntity sellerAuthenticationEntity3 = sellerAuthenticationRepository.findByUserName(parameter);
                if (ObjectUtils.isNotEmpty(sellerAuthenticationEntity))
                    return commonUtil.wrapToWrapperClass(sellerAuthenticationEntity, new ResultResponse(HttpStatus.OK));
                else if (ObjectUtils.isNotEmpty(sellerAuthenticationEntity1))
                    return commonUtil.wrapToWrapperClass(sellerAuthenticationEntity1, new ResultResponse(HttpStatus.OK));
                else if (ObjectUtils.isNotEmpty(sellerAuthenticationEntity2))
                    return commonUtil.wrapToWrapperClass(sellerAuthenticationEntity2, new ResultResponse(HttpStatus.OK));
                else if (ObjectUtils.isNotEmpty(sellerAuthenticationEntity3))
                    return commonUtil.wrapToWrapperClass(sellerAuthenticationEntity3, new ResultResponse(HttpStatus.OK));
                else
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> createNewSeller(SellerAuthenticationEntity sellerAuthenticationData, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                boolean isSupplierIdNotMatched = false;
                if(ObjectUtils.isNotEmpty(sellerAuthenticationData.getSupplierId())) {
                    SellerAuthenticationEntity sellerAuthenticationEntity = sellerAuthenticationRepository.findBySupplierId(sellerAuthenticationData.getSupplierId());
                    isSupplierIdNotMatched = ObjectUtils.isNotEmpty(sellerAuthenticationEntity);
                } else if(ObjectUtils.isNotEmpty(sellerAuthenticationData.getContactEmail())) {
                    SellerAuthenticationEntity sellerAuthenticationEntity = sellerAuthenticationRepository.findByContactEmail(sellerAuthenticationData.getContactEmail());
                    isSupplierIdNotMatched = ObjectUtils.isNotEmpty(sellerAuthenticationEntity);
                } else if(ObjectUtils.isNotEmpty(sellerAuthenticationData.getContactPhone())) {
                    SellerAuthenticationEntity sellerAuthenticationEntity = sellerAuthenticationRepository.findByContactPhone(sellerAuthenticationData.getContactPhone());
                    isSupplierIdNotMatched = ObjectUtils.isNotEmpty(sellerAuthenticationEntity);
                } else if(ObjectUtils.isNotEmpty(sellerAuthenticationData.getUserName())) {
                    SellerAuthenticationEntity sellerAuthenticationEntity = sellerAuthenticationRepository.findByUserName(sellerAuthenticationData.getUserName());
                    isSupplierIdNotMatched = ObjectUtils.isNotEmpty(sellerAuthenticationEntity);
                }
                if (isSupplierIdNotMatched)
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE, null, null);
                return sellerAuthenticationTransaction.createNewSeller(sellerAuthenticationData);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> updateExistingSupplier(SellerAuthenticationEntity sellerAuthenticationEntity, String errors) {
        try {
            if (ObjectUtils.isEmpty(sellerAuthenticationEntity.getSupplierId()) || sellerAuthenticationEntity.getSupplierId().equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            }
            SellerAuthenticationEntity sellerAuthenticationEntityBySupplierId = sellerAuthenticationRepository.findBySupplierId(sellerAuthenticationEntity.getSupplierId());
            if (ObjectUtils.isEmpty(sellerAuthenticationEntityBySupplierId))
                return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
//            else if (ObjectUtils.isNotEmpty(sellerAuthenticationEntityBySupplierId))
//                return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
//            else if (!sellerAuthenticationEntity.getUserName().equalsIgnoreCase(isIdNotMatched.get(0).getUserName()))
//                return serviceUtil.buildConflictData(StatusCodeEnum.USERNAME_UNMATCHED, null, null);
            return sellerAuthenticationTransaction.updateSeller(sellerAuthenticationEntity);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> deleteExistingSeller(String supplierId) {
        try {
            if (ObjectUtils.isEmpty(supplierId) || supplierId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                SellerAuthenticationEntity entityById = sellerAuthenticationRepository.findBySupplierId(supplierId);
                if (ObjectUtils.isEmpty(entityById)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    return sellerAuthenticationTransaction.deleteSeller(entityById);
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
                SellerAuthenticationEntity entityById = sellerAuthenticationRepository.findBySupplierId(supplierId);
                if (ObjectUtils.isEmpty(entityById))
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                return sellerAuthenticationTransaction.disActivateSellerId(entityById, status);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
