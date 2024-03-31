package com.lcb.ecommercebackend.project.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WrapperRequest<T> {
    @JsonProperty("result")
    public ResultRequest result;

    @JsonProperty("data")
    public T data;
}
