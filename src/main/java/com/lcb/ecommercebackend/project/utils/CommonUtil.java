package com.lcb.ecommercebackend.project.utils;

import com.lcb.ecommercebackend.project.model.dbSchema.DataCreationIdGenerationEntity;
import com.lcb.ecommercebackend.project.model.responses.ResultResponse;
import com.lcb.ecommercebackend.project.model.responses.ResponseWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommonUtil {

    public <T> ResponseWrapper<T> wrapToWrapperClass(T responseClass, ResultResponse resultResponse){
        ResponseWrapper<T> responseWrapper =  new ResponseWrapper<>();
        responseWrapper.setResult(resultResponse);
        responseWrapper.setData(responseClass);
        return responseWrapper;
    }

    public ZonedDateTime getCurrentDate(){
        return ZonedDateTime.now();
    }

    public String generateUniqueProductId() {
        return "PRD-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String generateUniqueId() {
        return "DIS-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Integer generateUniqueJrnlId(List<DataCreationIdGenerationEntity> dataCreationIdGenerationEntityList) {
        if (!ObjectUtils.isEmpty(dataCreationIdGenerationEntityList) && !dataCreationIdGenerationEntityList.isEmpty()) {
//            Math.abs(UUID.randomUUID().getMostSignificantBits())
            Optional<DataCreationIdGenerationEntity> latestEntity = dataCreationIdGenerationEntityList.stream().max(Comparator.comparing(DataCreationIdGenerationEntity::getCreatedAt));
            if (latestEntity.isPresent()) {
                return Integer.valueOf(latestEntity.get().getJrnlNum()) + 1;
            }
        }
        else {
            return Integer.valueOf(String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0,8));
        }
        return null;
    }
//        return UUID.randomUUID().toString().substring(0, 8);


}
