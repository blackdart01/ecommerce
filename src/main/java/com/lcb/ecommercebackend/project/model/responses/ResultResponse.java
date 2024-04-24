package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResultResponse {
    @JsonProperty("StatusCode")
    private Integer statusCode;
    @JsonProperty("Message")
    private String message;

    public ResultResponse(HttpStatus httpStatus) {
        this.statusCode = httpStatus.value();
        this.message = httpStatus.name();
    }
    public ResultResponse(HttpStatus httpStatus, String msg) {
        this.statusCode = httpStatus.value();
        this.message = msg;
    }

    public ResultResponse() {
    }
}
