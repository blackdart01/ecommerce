package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductResponse {

    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDescription")
    private String productDesc;
    @JsonProperty("productType")
    private String productType;
    @JsonProperty("productCost")
    private String productCost;
    @JsonProperty("category")
    private String category;
//    @JsonProperty("error_msg")
//    private String errorMsg;
//    @JsonProperty("status_code")
//    private Integer statusCode;
}
