package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.ProductCategoryEntity;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.StatusCodeEnum;
import com.lcb.ecommercebackend.project.repositories.DataCreationIdGenerationRepository;
import com.lcb.ecommercebackend.project.repositories.ProductCategoryRepository;
import com.lcb.ecommercebackend.project.services.transactionalService.CategoryTransaction;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import com.lcb.ecommercebackend.project.utils.ServiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private ServiceUtil serviceUtil;
    @Autowired
    private DataCreationIdGenerationRepository dataCreationIdGenerationRepository;
    @Autowired
    private CategoryTransaction categoryTransaction;

    public ResponseWrapper<List<ProductCategoryEntity>> getAllCategories() {
        List<ProductCategoryEntity> productCategoryEntityList = productCategoryRepository.findAll();
        if (ObjectUtils.isEmpty(productCategoryEntityList) || productCategoryEntityList.isEmpty())
            return commonUtil.wrapToWrapperClass(productCategoryEntityList, new ResultResponse(HttpStatus.CONFLICT, StatusCodeEnum.RECORD_NOT_FOUND.getMessgage()));
        return commonUtil.wrapToWrapperClass(productCategoryEntityList, serviceUtil.getAllCheck(productCategoryEntityList));
    }

    public ResponseWrapper<?> findByCategoryId(String categoryId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (ObjectUtils.isEmpty(categoryId) || categoryId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                ProductCategoryEntity categoryEntityByCategoryId = productCategoryRepository.findByCategoryId(categoryId);
                if (ObjectUtils.isEmpty(categoryEntityByCategoryId)) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    resultResponse.setStatusCode(HttpStatus.OK.value());
                    resultResponse.setMessage(HttpStatus.OK.name());
                    return commonUtil.wrapToWrapperClass(categoryEntityByCategoryId, resultResponse);
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
    public ResponseWrapper<?> createNewCategory(ProductCategoryEntity productCategoryEntity, String errors) {
        try {
            if (!ObjectUtils.isEmpty(errors))
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            return categoryTransaction.createNewCategory(productCategoryEntity);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public ResponseWrapper<?> updateExistingCategory(ProductCategoryEntity productCategoryEntity, String errors) {
        try {
            if (ObjectUtils.isEmpty(productCategoryEntity.getCategoryId()) || productCategoryEntity.getCategoryId().equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else if (!ObjectUtils.isEmpty(errors)) {
                return serviceUtil.buildConflictData(null, StatusCodeEnum.DATA_MISSING.getStatusCode(), errors);
            }
            List<ProductCategoryEntity> isIdNotMatched = getAllCategories().getData().stream().filter(category -> productCategoryEntity.getCategoryId().equalsIgnoreCase(category.getCategoryId())).toList();
            if (!isIdNotMatched.isEmpty()) {
                if (isIdNotMatched.size() > 1) {
                    return serviceUtil.buildConflictData(StatusCodeEnum.DUPLICATE_ID, null, null);
                }
                return categoryTransaction.updateExistingCategory(productCategoryEntity);
            }
            return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

        }
    }
    public ResponseWrapper<?> deleteExistingCategory(String categoryId) {
        try {
            if (ObjectUtils.isEmpty(categoryId) || categoryId.equalsIgnoreCase("null")) {
                return serviceUtil.buildConflictData(StatusCodeEnum.ID_NOT_FOUND, null, null);
            } else {
                ProductCategoryEntity productCategoryByCategoryId = productCategoryRepository.findByCategoryId(categoryId);
                if (ObjectUtils.isEmpty(productCategoryByCategoryId))
                    return serviceUtil.buildConflictData(StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                return categoryTransaction.deleteExistingCategory(productCategoryByCategoryId);
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }
}
