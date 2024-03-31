package com.lcb.ecommercebackend.project.model.dbSchema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="new_product")
public class ProductRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("productId")
    @Column(name = "productId")
    private Integer productId;

    @JsonProperty("productName")
    @Column(name="productName")
    private String productName;

    @JsonProperty("productDescription")
    @Column(name="productDescription")
    private String productDesc;

    @JsonProperty("productType")
    @Column(name="productType")
    private String productType;

    @JsonProperty("productCost")
    @Column(name="productCost")
    private String productCost;

    @JsonProperty("category")
    @Column(name="category")
    private String category;
}
