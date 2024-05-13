package com.lcb.ecommercebackend.project.services.transactionalService;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
import com.lcb.ecommercebackend.project.repositories.DiscountRepository;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class DiscountTransaction {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    CommonUtil commonUtil;
    @Autowired
    ServiceUtil serviceUtil;
    @Autowired
    ProductRepository productRepository;

    @Transactional
    public ResponseWrapper<?> createNewDiscount(DiscountEntity discountEntity) {
        try {
            ProductEntity productEntity = productRepository.findByProductId(ObjectUtils.isEmpty(discountEntity.getProductId()) ? "" : discountEntity.getProductId());
            if (ObjectUtils.isEmpty(productEntity))
                return serviceUtil.buildConflictData(StatusCodeEnum.PRODUCT_ID_NOT_EXIST, null, null);
            discountEntity.setCreatedAt(commonUtil.getCurrentDate());
            String discountId = commonUtil.generateUniqueId("DIS");
            discountEntity.setDiscountId(discountId);
            DiscountEntity discountEntitySaved = discountRepository.save(discountEntity);
            productEntity.setDiscountId(discountId);
            ProductEntity productEntitySaved = productRepository.save(productEntity);
            if (!ObjectUtils.isEmpty(productEntitySaved) && !ObjectUtils.isEmpty(discountEntitySaved))
                return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(discountEntitySaved.getId(), discountEntitySaved.getDiscountId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.DISCOUNT_CREATION_API), new ResultResponse(HttpStatus.OK));
            else
                return serviceUtil.buildConflictData(StatusCodeEnum.DATA_NOT_SAVED, null, null);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> updateExistingDiscount(DiscountEntity discountEntity) {
        try {
            DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountEntity.getDiscountId());
            if (!discountEntityByDiscountId.getProductId().equalsIgnoreCase(discountEntity.getProductId()))
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_UNMATCHED, null, null);
            discountEntityByDiscountId.setDiscountedPrice(discountEntity.getDiscountedPrice());
            discountEntityByDiscountId.setIsActive(discountEntity.getIsActive());
            discountEntityByDiscountId.setModifiedAt(commonUtil.getCurrentDate());
            discountEntityByDiscountId.setProductId(discountEntity.getProductId());
            DiscountEntity discountEntitySaved = discountRepository.save(discountEntityByDiscountId);
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(discountEntitySaved.getId(), discountEntitySaved.getDiscountId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.DISCOUNT_UPDATION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Transactional
    public ResponseWrapper<?> deleteExistingDiscount(DiscountEntity discountEntityByDiscountId) {
        try {
            discountRepository.deleteById(discountEntityByDiscountId.getId());
            return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(discountEntityByDiscountId.getId(), discountEntityByDiscountId.getDiscountId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.DISCOUNT_DELETION_API), new ResultResponse(HttpStatus.OK));
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}