package com.lcb.ecommercebackend.project.utils;

import com.lcb.ecommercebackend.project.model.dbSchema.DataCreationIdGenerationEntity;
import com.lcb.ecommercebackend.project.model.responses.*;
import com.lcb.ecommercebackend.project.repositories.DataCreationIdGenerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;

@Service
public class ServiceUtil extends CommonUtil {
@Autowired
private DataCreationIdGenerationRepository dataCreationIdGenerationRepository;
    public ResultResponse getAllCheck(Object anyClass){
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setStatusCode((ObjectUtils.isEmpty(anyClass) ? HttpStatus.NO_CONTENT.value() : HttpStatus.OK.value()));
        resultResponse.setMessage((ObjectUtils.isEmpty(anyClass) ? StatusCodeEnum.NO_DATA.getMessgage() : StatusCodeEnum.SUCCESS.getMessgage()));
        return resultResponse;
    }
    public ResponseWrapper<?> buildConflictData(StatusCodeEnum statusCodeEnum, Integer statusCode, String msg){
        ErrorResponse errorResponse = ObjectUtils.isEmpty(statusCodeEnum) ? new ErrorResponse(statusCode, msg): new ErrorResponse(statusCodeEnum);
        return wrapToWrapperClass(errorResponse, new ResultResponse(HttpStatus.CONFLICT));
    }

    public Object buildJrnlNum(Integer id, String identifier, String msg, StatusCodeEnum statusCodeEnum){
        DataCreationIdGenerationEntity dataCreationIdGenerationEntity = new DataCreationIdGenerationEntity();
        String jrnlNum = null;
        try {
            dataCreationIdGenerationEntity.setApiName(statusCodeEnum.getMessgage());
            dataCreationIdGenerationEntity.setIdSaved(String.valueOf(id));
            dataCreationIdGenerationEntity.setCreatedAt(ZonedDateTime.now());
            dataCreationIdGenerationEntity.setRequestMethod(statusCodeEnum.getRequestCode());
            jrnlNum = String.valueOf(generateUniqueJrnlId(dataCreationIdGenerationRepository.findAll()));
            dataCreationIdGenerationEntity.setJrnlNum(jrnlNum);
            dataCreationIdGenerationRepository.save(dataCreationIdGenerationEntity);
            msg = ObjectUtils.isEmpty(identifier) || identifier.equalsIgnoreCase("null") ? msg : msg + " " + identifier;
        } catch (Exception e){
            return buildConflictData(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return new SuccessResponse(Integer.valueOf(ObjectUtils.isEmpty(jrnlNum)?"":jrnlNum), msg);
    }

    public Boolean someMethod(Object entityClass) {
        return true;
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        List<String> keyList;
//        Map<String, Object> map = objectMapper.convertValue(entityClass, Map.class);
//        if (!ObjectUtils.isEmpty(map) && !map.isEmpty()) {
//            keyList = new ArrayList<>(map.keySet());
//            for (String key : keyList) {
//                if (!key.equalsIgnoreCase("id") && !key.equalsIgnoreCase("created_at") && !key.equalsIgnoreCase("modified_at") && ObjectUtils.isEmpty(map.get(key)))
//                    return false;
//            }
//        }
//        return true;
    }

}
