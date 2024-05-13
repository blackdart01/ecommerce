package com.lcb.ecommercebackend.project.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ProductCategoryResponse {

    @JsonProperty("categoryId")
    private Integer categoryId;
    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("categoryDescription")
    private String categoryDescription;
    @JsonProperty("createdAt")
    private ZonedDateTime createdAt;
    @JsonProperty("modifiedAt")
    private ZonedDateTime modifiedAt;
    @JsonProperty("deletedAt")
    private ZonedDateTime deletedAt;
}
