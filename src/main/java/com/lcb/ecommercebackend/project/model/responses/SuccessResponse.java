package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SuccessResponse {
    @JsonProperty("JrnlNum")
    private Integer jrnlNum;
    @JsonProperty("RcptData")
    private String rcptData="";

    public SuccessResponse(StatusCodeEnum statusCodeEnum) {
        this.jrnlNum = statusCodeEnum.getStatusCode();
        this.rcptData = statusCodeEnum.getMessgage();
    }
    public SuccessResponse(Integer jrnlNum, String rcptData) {
        this.jrnlNum = jrnlNum;
        this.rcptData = rcptData;
    }
}
