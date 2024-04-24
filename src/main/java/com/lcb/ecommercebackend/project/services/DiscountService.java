package com.lcb.ecommercebackend.project.services;

import com.lcb.ecommercebackend.project.model.dbSchema.DiscountEntity;
import com.lcb.ecommercebackend.project.model.dbSchema.ProductEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.DataCreationIdGenerationRepository;
import com.lcb.ecommercebackend.project.repositories.DiscountRepository;
import com.lcb.ecommercebackend.project.repositories.ProductRepository;
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
        ResponseWrapper<?> responseWrapper;
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
                } else {
                    ProductEntity productEntity = productRepository.findByProductId(ObjectUtils.isEmpty(discountEntity.getProductId()) ? "" : discountEntity.getProductId());
                    if (ObjectUtils.isEmpty(productEntity))
                        return serviceUtil.buildConflictData(StatusCodeEnum.PRODUCT_ID_NOT_EXIST, null, null);
                    else {
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
                    }
                }
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
                    } else {
                        DiscountEntity discountEntityByDiscountId = discountRepository.findByDiscountId(discountEntity.getDiscountId());
                        if (!discountEntityByDiscountId.getProductId().equalsIgnoreCase(discountEntity.getProductId()))
                            return serviceUtil.buildConflictData(StatusCodeEnum.ID_UNMATCHED, null, null);
                        discountEntityByDiscountId.setDiscountedPrice(discountEntity.getDiscountedPrice());
                        discountEntityByDiscountId.setIsActive(discountEntity.getIsActive());
                        discountEntityByDiscountId.setModifiedAt(commonUtil.getCurrentDate());
                        discountEntityByDiscountId.setProductId(discountEntity.getProductId());
                        DiscountEntity discountEntitySaved = discountRepository.save(discountEntityByDiscountId);
                        return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(discountEntitySaved.getId(), discountEntitySaved.getDiscountId(), StatusCodeEnum.OK_MSG.getMessgage(), StatusCodeEnum.DISCOUNT_UPDATION_API), new ResultResponse(HttpStatus.OK));
                    }
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
                } else {
                    discountRepository.deleteById(discountEntityByDiscountId.getId());
                    return commonUtil.wrapToWrapperClass(serviceUtil.buildJrnlNum(discountEntityByDiscountId.getId(), discountEntityByDiscountId.getDiscountId(), StatusCodeEnum.OK_DELETED.getMessgage(), StatusCodeEnum.DISCOUNT_DELETION_API), new ResultResponse(HttpStatus.OK));
                }
            }
        } catch (Exception e) {
            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

//    public Boolean someMethod(DiscountEntity discountEntity) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        List<String> keyList;
//        Map<String, Object> map = objectMapper.convertValue(discountEntity, Map.class);
//        if (!ObjectUtils.isEmpty(map) && !map.isEmpty()) {
//            keyList = new ArrayList<>(map.keySet());
//            for (String key : keyList) {
//                if (!key.equalsIgnoreCase("id") && ObjectUtils.isEmpty(map.get(key)))
//                    return false;
//            }
//        }
//        return true;
//    }
//    public Object buildJrnlNum(Integer id, String identifier, String msg, StatusCodeEnum statusCodeEnum){
//        DataCreationIdGenerationEntity dataCreationIdGenerationEntity = new DataCreationIdGenerationEntity();
//        String jrnlNum = null;
//        try {
//            dataCreationIdGenerationEntity.setApiName(statusCodeEnum.getMessgage());
//            dataCreationIdGenerationEntity.setIdSaved(String.valueOf(id));
//            dataCreationIdGenerationEntity.setCreatedAt(ZonedDateTime.now());
//            dataCreationIdGenerationEntity.setRequestMethod(statusCodeEnum.getRequestCode());
//            jrnlNum = String.valueOf(commonUtil.generateUniqueJrnlId(dataCreationIdGenerationRepository.findAll()));
//            dataCreationIdGenerationEntity.setJrnlNum(jrnlNum);
//            dataCreationIdGenerationRepository.save(dataCreationIdGenerationEntity);
//            msg = ObjectUtils.isEmpty(identifier) || identifier.equalsIgnoreCase("null") ? msg : msg + " " + identifier;
//        } catch (Exception e){
//            return serviceUtil.buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//        }
//        return new SuccessResponse(Integer.valueOf(ObjectUtils.isEmpty(jrnlNum)?"":jrnlNum), msg);
//    }

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
