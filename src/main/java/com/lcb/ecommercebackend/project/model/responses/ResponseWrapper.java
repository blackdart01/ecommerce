package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseWrapper<T> {
    @JsonProperty("result")
    public ResultResponse result;

    @JsonProperty("data")
    public T data;
}
