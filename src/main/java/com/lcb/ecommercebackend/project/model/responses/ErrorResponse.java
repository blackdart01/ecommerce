package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {
    @JsonProperty("SupOvrrd")
    private int supOvrrd;
    @JsonProperty("ErrCode")
    private int errCode;
    @JsonProperty("ErrMsg")
    private String errMsg;

    public ErrorResponse(StatusCodeEnum statusCodeEnum) {
        this.supOvrrd = 0;
        this.errCode = statusCodeEnum.getStatusCode();
        this.errMsg = statusCodeEnum.getMessgage();
    }
    public ErrorResponse(int errCode, String errMsg) {
        this.supOvrrd = 0;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ErrorResponse() {
    }
}
