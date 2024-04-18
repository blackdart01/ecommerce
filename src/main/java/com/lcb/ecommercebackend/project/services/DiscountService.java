package com.lcb.ecommercebackend.project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lcb.ecommercebackend.project.model.dbSchema.DataCreationIdGenerationEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.DataCreationIdGenerationRepository;
import com.lcb.ecommercebackend.project.repositories.DiscountRepository;
import com.lcb.ecommercebackend.project.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private DataCreationIdGenerationRepository dataCreationIdGenerationRepository;

    public ResponseWrapper<List<DiscountEntity>> getAllDiscounts() {
        List<DiscountEntity> discountEntityList = discountRepository.findAll();
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setStatusCode((ObjectUtils.isEmpty(discountEntityList) ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value()));
        resultResponse.setMessage((ObjectUtils.isEmpty(discountEntityList) ? StatusCodeEnum.NO_DATA.getMessgage() : StatusCodeEnum.SUCCESS.getMessgage()));
        return commonUtil.wrapToWrapperClass(discountEntityList, resultResponse);
    }

    public ResponseWrapper<?> findByDiscountId(String discountId) {
        ResultResponse resultResponse = new ResultResponse();
        ResponseWrapper<?> responseWrapper;
        if (ObjectUtils.isEmpty(discountId) && discountId.equalsIgnoreCase("null")) {
            responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.ID_NOT_FOUND, null, null);
        } else {
            try {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    resultResponse.setStatusCode(HttpStatus.OK.value());
                    resultResponse.setMessage(HttpStatus.OK.name());
                    responseWrapper = commonUtil.wrapToWrapperClass(discountEntityByDiscountId, resultResponse);
                }
            } catch (Exception e) {
                responseWrapper = buildConflictData(resultResponse, null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return responseWrapper;
    }
    public ResponseWrapper<?> findByProductId(String productId) {
        ResultResponse resultResponse = new ResultResponse();
        ResponseWrapper<?> responseWrapper;
        if (ObjectUtils.isEmpty(productId) && productId.equalsIgnoreCase("null")) {
            responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.ID_NOT_FOUND, null, null);
        } else {
            try {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByProductId(productId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    resultResponse.setStatusCode(HttpStatus.OK.value());
                    resultResponse.setMessage(HttpStatus.OK.name());
                    responseWrapper = commonUtil.wrapToWrapperClass(discountEntityByDiscountId, resultResponse);
                }
            } catch (Exception e) {
                responseWrapper = buildConflictData(resultResponse, null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return responseWrapper;
    }

    public ResponseWrapper<?> createNewDiscount(DiscountEntity discountEntity) {
        ResultResponse resultResponse = new ResultResponse();
        ResponseWrapper<?> responseWrapper;
        try {
            ResponseWrapper<List<DiscountEntity>> allDiscountsWrappper = getAllDiscounts();
            boolean isIdNotMatched = allDiscountsWrappper.getData().stream().filter(discountEntity1 -> discountEntity.getProductId().equalsIgnoreCase(discountEntity1.getProductId())).findFirst().isEmpty();
            if (!isIdNotMatched) {
                responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.DUPLICATE_ID, null, null);
            } else {
                resultResponse.setMessage(HttpStatus.OK.name());
                resultResponse.setStatusCode(HttpStatus.OK.value());
                discountEntity.setCreatedAt(ZonedDateTime.now());
                discountEntity.setDiscountId(commonUtil.generateUniqueId());
                DiscountEntity discountEntitySaved = discountRepository.save(discountEntity);
                responseWrapper = commonUtil.wrapToWrapperClass(buildJrnlNum(discountEntitySaved.getId(), discountEntitySaved.getDiscountId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.DISCOUNT_CREATION_API), resultResponse);
            }
        } catch (Exception e) {
            resultResponse.setStatusCode(HttpStatus.CONFLICT.value());
            resultResponse.setMessage(HttpStatus.CONFLICT.name());
            ErrorResponse errorResponse;
            if (ObjectUtils.isEmpty(discountEntity.getProductId()))
                errorResponse = new ErrorResponse(StatusCodeEnum.ID_NOT_FOUND);
            else
                errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            responseWrapper = commonUtil.wrapToWrapperClass(errorResponse, resultResponse);
        }
        return responseWrapper;
    }
    public ResponseWrapper<?> updateExistingDiscount(DiscountEntity discountEntity) {
        ResponseWrapper<?> responseWrapper;
        ResultResponse resultResponse = new ResultResponse();
        if (ObjectUtils.isEmpty(discountEntity.getDiscountId()) || discountEntity.getDiscountId().equalsIgnoreCase("null")) {
            responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.ID_NOT_FOUND, null, null);
        } else {
            try {
                List<DiscountEntity> isIdNotMatched = getAllDiscounts().getData().stream().filter(discountEntity1 -> discountEntity.getDiscountId().equalsIgnoreCase(discountEntity1.getDiscountId())).toList();
                if (!isIdNotMatched.isEmpty()) {
                    if (isIdNotMatched.size() > 1) {
                        responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.DUPLICATE_ID, null, null);
                    } else {
                        if (someMethod(discountEntity)) {
                            DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountEntity.getDiscountId());
                            discountEntityByDiscountId.setDiscountedPrice(discountEntity.getDiscountedPrice());
                            discountEntityByDiscountId.setCreatedAt(discountEntity.getCreatedAt());
                            discountEntityByDiscountId.setIsActive(discountEntity.getIsActive());
                            discountEntityByDiscountId.setModifiedAt(discountEntity.getModifiedAt());
                            discountEntityByDiscountId.setProductId(discountEntity.getProductId());
                            DiscountEntity discountEntitySaved = discountRepository.save(discountEntityByDiscountId);
                            resultResponse.setMessage(HttpStatus.OK.name());
                            resultResponse.setStatusCode(HttpStatus.OK.value());
                            responseWrapper = commonUtil.wrapToWrapperClass(buildJrnlNum(discountEntitySaved.getId(), discountEntitySaved.getDiscountId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.DISCOUNT_UPDATION_API), resultResponse);
                        } else {
                            responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.DATA_MISSING, null, null);
                        }
                    }
                } else {
                    responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                }
            } catch (Exception e) {
                responseWrapper = buildConflictData(resultResponse, null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return responseWrapper;
    }

    public ResponseWrapper<?> deleteExistingDiscount(String discountId) {
        ResultResponse resultResponse = new ResultResponse();
        ResponseWrapper<?> responseWrapper;
        if (ObjectUtils.isEmpty(discountId) || discountId.equalsIgnoreCase("null")) {
            responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.ID_NOT_FOUND, null, null);
        } else {
            try {
                DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountId);
                if (ObjectUtils.isEmpty(discountEntityByDiscountId)) {
                    responseWrapper = buildConflictData(resultResponse, StatusCodeEnum.RECORD_NOT_FOUND, null, null);
                } else {
                    discountRepository.deleteById(discountEntityByDiscountId.getId());
                    resultResponse.setStatusCode(HttpStatus.OK.value());
                    resultResponse.setMessage(HttpStatus.OK.name());
                    responseWrapper = commonUtil.wrapToWrapperClass(buildJrnlNum(discountEntityByDiscountId.getId(), discountEntityByDiscountId.getDiscountId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.DISCOUNT_DELETION_API), resultResponse);
                }
            } catch (Exception e) {
                responseWrapper = buildConflictData(resultResponse, null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return responseWrapper;
    }

    public Boolean someMethod(DiscountEntity discountEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<String> keyList;
        Map<String, Object> map = objectMapper.convertValue(discountEntity, Map.class);
        if (!ObjectUtils.isEmpty(map) && !map.isEmpty()) {
            keyList = new ArrayList<>(map.keySet());
            for (String key : keyList) {
                if (!key.equalsIgnoreCase("id") && ObjectUtils.isEmpty(map.get(key)))
                    return false;
            }
        }
        return true;
    }
    public SuccessResponse buildJrnlNum(Integer id, String identifier, String msg, StatusCodeEnum statusCodeEnum){
        DataCreationIdGenerationEntity dataCreationIdGenerationEntity = new DataCreationIdGenerationEntity();
        dataCreationIdGenerationEntity.setApiName(statusCodeEnum.getMessgage());
        dataCreationIdGenerationEntity.setIdSaved(String.valueOf(id));
        dataCreationIdGenerationEntity.setCreatedAt(ZonedDateTime.now());
        dataCreationIdGenerationEntity.setRequestMethod(statusCodeEnum.getRequestCode());
        String jrnlNum = String.valueOf(commonUtil.generateUniqueJrnlId(dataCreationIdGenerationRepository.findAll()));
        dataCreationIdGenerationEntity.setJrnlNum(jrnlNum);
        dataCreationIdGenerationRepository.save(dataCreationIdGenerationEntity);
        msg = ObjectUtils.isEmpty(identifier) || identifier.equalsIgnoreCase("null")?msg: msg+" "+identifier;
        return new SuccessResponse(Integer.valueOf(jrnlNum), msg);
    }
    public ResponseWrapper<?> buildConflictData(ResultResponse resultResponse, StatusCodeEnum statusCodeEnum, Integer statusCode, String msg){
        resultResponse.setStatusCode(HttpStatus.CONFLICT.value());
        resultResponse.setMessage(HttpStatus.CONFLICT.name());
        ErrorResponse errorResponse = ObjectUtils.isEmpty(statusCodeEnum) ? new ErrorResponse(statusCode, msg): new ErrorResponse(statusCodeEnum);
        return commonUtil.wrapToWrapperClass(errorResponse, resultResponse);
    }
//    public List<String> logJsonPropertyNames(DiscountEntity discount) throws NoSuchFieldException, IllegalAccessException {
//        Class<?> clazz = discount.getClass();
//        List<String> fields = new ArrayList<>();
//        for (Field field : clazz.getDeclaredFields()) {
//            if (field.isAnnotationPresent(JsonProperty.class)) {
//                JsonProperty annotation = field.getAnnotation(JsonProperty.class);
//                fields.add(annotation.value().isEmpty() ? field.getName() : annotation.value());
//            }
//        }
//        return fields;
//    }

//    public Boolean someMethod(List<String> fields, DiscountEntity discountEntity){
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        Map<String, Object> map = objectMapper.convertValue(discountEntity, Map.class);
//        List<String> discountEntityList = new ArrayList<>();
//        if(!ObjectUtils.isEmpty(map) && !map.isEmpty()){
//            discountEntityList = new ArrayList<>(map.keySet());
//            if(!discountEntityList.isEmpty()){
//                for(String field : fields){
//                    if(!discountEntityList.contains(field)) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }

}
