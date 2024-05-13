package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.DataCreationIdGenerationRepository;
import com.lcb.ecommercebackend.project.repositories.DiscountRepository;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.services.transactionalService.DiscountTransaction;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DataCreationIdGenerationRepository dataCreationIdGenerationRepository;
    @Autowired
    private DiscountTransaction discountTransaction;

    public ResponseWrapper<List<DiscountEntity>> getAllDiscounts() {
        List<DiscountEntity> discountEntityList = discountRepository.findAll();
        if (ObjectUtils.isEmpty(discountEntityList) || discountEntityList.isEmpty())
            return commonUtil.wrapToWrapperClass(discountEntityList, new ResultResponse(HttpStatus.CONFLICT, StatusCodeEnum.RECORD_NOT_FOUND.getMessgage()));
        return commonUtil.wrapToWrapperClass(discountEntityList, serviceUtil.getAllCheck(discountEntityList));
    }

    public ResponseWrapper<?> findByDiscountId(String discountId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (ObjectUtils.isEmpty(discountId) || discountId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    resultResponse.setStatusCode(HttpStatus.OK.value());
                    resultResponse.setMessage(HttpStatus.OK.name());
                    return commonUtil.wrapToWrapperClass(discountEntityByDiscountId, resultResponse);
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> findByProductId(String productId) {
        try {
            if (ObjectUtils.isEmpty(productId) || productId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByProductId(productId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    return commonUtil.wrapToWrapperClass(discountEntityByDiscountId, new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> createNewDiscount(DiscountEntity discountEntity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                ResponseWrapper<List<DiscountEntity>> allDiscountsWrappper = getAllDiscounts();
                boolean isProductIdNotMatched = allDiscountsWrappper.getData().stream().filter(discountEntity1 -> discountEntity.getProductId().equalsIgnoreCase(discountEntity1.getProductId())).findFirst().isEmpty();
                if (ObjectUtils.isEmpty(discountEntity.getProductId()) || discountEntity.getProductId().equalsIgnoreCase("null")) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
                } else if (!isProductIdNotMatched) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
                }
                return discountTransaction.createNewDiscount(discountEntity);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    public ResponseWrapper<?> updateExistingDiscount(DiscountEntity discountEntity, String errors) {
        try {
            if (ObjectUtils.isEmpty(discountEntity.getDiscountId()) || discountEntity.getDiscountId().equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            } else {
                List<DiscountEntity> isIdNotMatched = getAllDiscounts().getData().stream().filter(discountEntity1 -> discountEntity.getDiscountId().equalsIgnoreCase(discountEntity1.getDiscountId())).toList();
                if (!isIdNotMatched.isEmpty()) {
                    if (isIdNotMatched.size() > 1) {
                        return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
                    }
                    return discountTransaction.updateExistingDiscount(discountEntity);
                } else {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    public ResponseWrapper<?> deleteExistingDiscount(String discountId) {
        try {
            if (ObjectUtils.isEmpty(discountId) || discountId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                }
                return discountTransaction.deleteExistingDiscount(discountEntityByDiscountId);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
