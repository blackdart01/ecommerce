package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultResponse {
    @JsonProperty("StatusCode")
    private Integer statusCode;
    @JsonProperty("Message")
    private String message;
}
