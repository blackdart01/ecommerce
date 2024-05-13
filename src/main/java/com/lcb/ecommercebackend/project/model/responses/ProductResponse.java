package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ProductResponse {

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("productDescription")
    private String productDesc;
    @JsonProperty("price")
    private String price;
    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("createdAt")
    private ZonedDateTime createdAt;
    @JsonProperty("modifiedAt")
    private ZonedDateTime modifiedAt;
    @JsonProperty("deletedAt")
    private ZonedDateTime deletedAt;
}
