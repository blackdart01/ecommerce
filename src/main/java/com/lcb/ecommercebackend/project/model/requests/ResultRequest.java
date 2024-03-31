package com.lcb.ecommercebackend.project.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultRequest {
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("errorMsg")
    private String errorMsg;
}
